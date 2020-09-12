package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.helper.WindowHelper;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public abstract class MixinEntityRenderer implements IMixinEntityRenderer {

    @Shadow
    private float fovModifierHand;

    @Shadow
    private float fovModifierHandPrev;

    @Shadow
    protected abstract void loadShader(ResourceLocation p_loadShader_1_);

    @Unique
    private final Consumer<Float> renderEvent = partialTicks -> new EventRender3D(partialTicks).broadcast();

    @Unique
    private final Consumer<Float> renderEventNoBobbing = partialTicks -> new EventRender3DNoBobbing(partialTicks).broadcast();

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHand(float partialTicks, CallbackInfo ci) {
        if (!WindowHelper.isMinimized()) {
            renderEvent.accept(partialTicks);
            // FIXME: Transform camera
            renderEventNoBobbing.accept(partialTicks);
        }
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        EventHurtcam event = new EventHurtcam();
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "updateCameraAndRender(FJZ)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiIngame.renderGameOverlay(F)V"))
    private void onRender2D(CallbackInfo cb) {
        if (!WindowHelper.isMinimized()) {
            // Chat queue
            Runnable operation = ChatHud.getChatMessageQueue().poll();
            if (operation != null) {
                operation.run();
            }
            // Other actions
            operation = Minecraft.RENDER_THREAD.poll();
            if (operation != null) {
                operation.run();
            }
            new EventRender2D(0f).broadcast();
        }
    }

    @Override
    public void loadCustomShader(MinecraftIdentifier location) {
        loadShader(location);
    }

    /* FIXME
    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ProjectileUtil;rayTrace(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/BoundingBox;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"))
    private EntityHitResult onRayTraceDistance(Entity entity, Vec3d vec3d, Vec3d vec3d2, BoundingBox box, Predicate<Entity> predicate, double distance) {
        return ProjectileUtil.rayTrace(entity, vec3d, vec3d2, box, predicate, (boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "BYPASS_REACH_LIMIT", false) ? 0d : distance);
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D", ordinal = 1))
    private double onDistance(Vec3d self, Vec3d vec3d) {
        return (boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "BYPASS_REACH_LIMIT", false) ? 2D : self.squaredDistanceTo(vec3d);
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerplayerController;hasExtendedReach()Z"))
    private boolean onTest(ClientPlayerplayerController clientPlayerplayerController) {
        return !(boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "BYPASS_REACH_LIMIT", false) && clientPlayerplayerController.hasExtendedReach();
    }*/

    @Override
    public float getFovMultiplier() {
        return fovModifierHand;
    }

    @Override
    public void updateFovMultiplier(float newFov) {
        fovModifierHandPrev = newFov;
        fovModifierHand = newFov;
    }

    @Inject(method = "addRainParticles", at = @At("HEAD"), cancellable = true)
    private void addRainParticles(CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderRainSnow", at = @At("HEAD"), cancellable = true)
    private void renderRainSnow(float partialTicks, CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @ModifyArg(method = "updateCameraAndRender(FJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isSpectator()Z"))
    public boolean isSpectator(boolean spectator) {
        return spectator || CameraEntityMan.isActive();
    }

}
