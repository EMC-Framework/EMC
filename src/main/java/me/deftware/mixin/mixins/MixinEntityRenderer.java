package me.deftware.mixin.mixins;

import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.event.events.EventHurtcam;
import me.deftware.client.framework.event.events.EventRender2D;
import me.deftware.client.framework.event.events.EventRender3D;
import me.deftware.client.framework.event.events.EventWeather;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.wrappers.IResourceLocation;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(GameRenderer.class)
public abstract class MixinEntityRenderer implements IMixinEntityRenderer {

    @Shadow
    private boolean renderHand;

    private float partialTicks = 0;

    @Shadow private float viewDistance;

    @Shadow
    private float movementFovMultiplier;

    @Shadow
    private float lastMovementFovMultiplier;

    @Shadow
    protected abstract void loadShader(Identifier p_loadShader_1_);

    @Inject(method = "renderRain", at = @At("HEAD"), cancellable = true)
    private void addRainParticles(CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void renderRainSnow(float partialTicks, CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void updateCameraAndRender(float partialTicks, long finishTimeNano, CallbackInfo ci) {
        this.partialTicks = partialTicks;
    }

    @Redirect(method = "renderCenter", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = 180))
    private boolean updateCameraAndRender_renderHand(GameRenderer self) {
        new EventRender3D(partialTicks).broadcast();
        return renderHand;
    }

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        EventHurtcam event = new EventHurtcam();
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/hud/InGameHud.draw(F)V"))
    private void onRender2D(CallbackInfo cb) {
        Runnable operation = ChatHud.getChatMessageQueue().poll();
        if (operation != null) {
            operation.run();
        }
        new EventRender2D(0f).broadcast();
    }

    @Override
    public void loadCustomShader(IResourceLocation location) {
        loadShader(location);
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ProjectileUtil;rayTrace(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/BoundingBox;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"))
    private EntityHitResult onRayTraceDistance(Entity entity, Vec3d vec3d, Vec3d vec3d2, BoundingBox box, Predicate<Entity> predicate, double d) {
        return ProjectileUtil.rayTrace(entity, vec3d, vec3d2, box, predicate, (boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "BYPASS_REACH_LIMIT", false) ? 0d : d);
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D", ordinal = 1))
    private double onDistance(Vec3d self, Vec3d vec3d) {
        return (boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "BYPASS_REACH_LIMIT", false) ? 2D : self.squaredDistanceTo(vec3d);
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasExtendedReach()Z"))
    private boolean onTest(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        return !(boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "BYPASS_REACH_LIMIT", false) && clientPlayerInteractionManager.hasExtendedReach();
    }

    @Override
    public float getFovMultiplier() {
        return movementFovMultiplier;
    }

    @Override
    public void updateFovMultiplier(float newFov) {
        lastMovementFovMultiplier = newFov;
        movementFovMultiplier = newFov;
    }

}
