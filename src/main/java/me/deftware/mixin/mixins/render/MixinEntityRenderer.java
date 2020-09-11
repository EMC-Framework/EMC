package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.platform.GlStateManager;
import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.helper.GlStateHelper;
import me.deftware.client.framework.helper.WindowHelper;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientPlayerplayerController;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(GameRenderer.class)
public abstract class MixinEntityRenderer implements IMixinEntityRenderer {

    @Shadow
    private float movementFovMultiplier;

    @Shadow
    private float lastMovementFovMultiplier;

    @Shadow
    protected abstract void loadShader(Identifier identifier);

    @Shadow
    @Final
    private Camera camera;

    @Shadow private double field_4005;

    @Shadow private double field_3988;

    @Shadow private double field_4004;

    @Shadow protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);

    @Shadow @Final private Minecraft client;

    @Shadow private float viewDistance;

    @Unique
    private final Consumer<Float> renderEvent = partialTicks -> new EventRender3D(partialTicks).broadcast();

    @Unique
    private final Consumer<Float> renderEventNoBobbing = partialTicks -> new EventRender3DNoBobbing(partialTicks).broadcast();

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHand(Camera camera, float partialTicks, CallbackInfo ci) {
        if (!WindowHelper.isMinimized()) {
            renderEvent.accept(partialTicks);
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            if (this.field_4005 != 1.0D) {
                GlStateManager.translatef((float) this.field_3988, (float) -this.field_4004, 0.0F);
                GlStateManager.scaled(this.field_4005, this.field_4005, 1.0D);
            }
            GlStateManager.multMatrix(Matrix4f.method_4929(this.getFov(this.camera, partialTicks, true), (float) this.client.window.getFramebufferWidth() / (float) this.client.window.getFramebufferHeight(), 0.05F, this.viewDistance * MathHelper.SQUARE_ROOT_OF_TWO));
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            camera.update(this.client.world, this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity(), this.client.options.perspective > 0, this.client.options.perspective == 2, partialTicks);
            renderEventNoBobbing.accept(partialTicks);
            GlStateHelper.enableLighting();
        }
    }

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        EventHurtcam event = new EventHurtcam();
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;draw(F)V"))
    private void onRender2D(CallbackInfo ci) {
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

    @Inject(method = "renderRain", at = @At("HEAD"), cancellable = true)
    private void renderRain(CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void renderWeather(float tickDelta, CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();

        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @ModifyArg(method = "renderCenter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setUpTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/VisibleRegion;IZ)V"), index = 3)
    public boolean isSpectator(boolean spectator) {
        return spectator || CameraEntityMan.isActive();
    }

}
