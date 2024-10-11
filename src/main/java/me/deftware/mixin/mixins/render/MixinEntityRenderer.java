package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.helper.GlStateHelper;
import me.deftware.client.framework.helper.WindowHelper;
import me.deftware.client.framework.render.shader.Shader;
import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;
import java.util.function.Supplier;

@Mixin(GameRenderer.class)
public abstract class MixinEntityRenderer implements IMixinEntityRenderer {

    @Shadow
    private float fovMultiplier;

    @Shadow
    private float lastFovMultiplier;

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private boolean postProcessorEnabled;

    @Shadow
    @Final
    private BufferBuilderStorage buffers;

    @Shadow public abstract Matrix4f getBasicProjectionMatrix(float f);

    @Shadow protected abstract float getFov(Camera camera, float tickDelta, boolean changingFov);

    @Shadow
    @Final
    private Pool pool;

    @Unique
    private final EventRender3D eventRender3D = new EventRender3D();

    @Unique
    private final EventRender3DNoBobbing eventRender3DNoBobbing = new EventRender3DNoBobbing();

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHand(Camera camera, float partialTicks, Matrix4f matrix4f, CallbackInfo ci) {
       if (!WindowHelper.isMinimized()) {
           // Normal 3d event
           MatrixStack matrixStack = new MatrixStack();
           matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
           matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
           DrawContext drawContext = MixinDrawContextInvoker.create(this.client, matrixStack, this.buffers.getEntityVertexConsumers());
           loadPushPop(() -> eventRender3D, drawContext, partialTicks);
           // Camera model stack without bobbing applied
           drawContext = new DrawContext(this.client, this.buffers.getEntityVertexConsumers());
           MatrixStack matrix = drawContext.getMatrices();
           matrix.push();
           var fov = getFov(camera, partialTicks, true);
           Matrix4f matrix4f2 = getBasicProjectionMatrix(fov);
           matrix.peek().getPositionMatrix().mul(matrix4f2);
           RenderSystem.setProjectionMatrix(matrix.peek().getPositionMatrix(), ProjectionType.ORTHOGRAPHIC);
           // Camera transformation stack
           matrix.pop();
           matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.camera.getPitch()));
           matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.camera.getYaw() + 180f));
           loadPushPop(() -> eventRender3DNoBobbing, drawContext, partialTicks);
           drawContext.draw();
           // Reset projection
           RenderSystem.setProjectionMatrix(matrixStack.peek().getPositionMatrix(), ProjectionType.ORTHOGRAPHIC);
           GlStateHelper.enableLighting();
       }
    }

    @Unique
    private <T extends EventRender3D> void loadPushPop(Supplier<T> supplier, DrawContext context, float partialTicks) {
        T event = supplier.get();
        event.create(partialTicks);
        event.setContext(GLX.of(context));
        event.broadcast();
    }

    @Unique
    private final EventHurtcam eventHurtcam = new EventHurtcam();

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(MatrixStack stack, float partialTicks, CallbackInfo ci) {
        eventHurtcam.setCanceled(false);
        eventHurtcam.broadcast();
        if (eventHurtcam.isCanceled()) {
            ci.cancel();
        }
    }

    @Unique
    private final EventRender2D eventRender2D = new EventRender2D();

    @Unique
    private final EventMatrixRender eventMatrixRender = new EventMatrixRender();

    @Redirect(method = "render", at = @At(value = "INVOKE", opcode = 180, target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
    private void onRender2D(InGameHud inGameHud, DrawContext context, RenderTickCounter tickCounter) {
        if (!WindowHelper.isMinimized()) {
            GLX glx = GLX.of(context);
            float tickDelta = tickCounter.getTickDelta(true);
            // Minecraft modifies opacity underwater
            glx.color(1, 1, 1, 1);
            eventRender2D.setContext(glx);
            eventRender2D.create(tickDelta).broadcast();
            // Render with custom matrix
            RenderStack.reloadCustomMatrix();
            RenderStack.setupGl();
            eventMatrixRender.setContext(glx);
            eventMatrixRender.create(tickDelta).broadcast();
            RenderStack.restoreGl();
            RenderStack.reloadMinecraftMatrix();
        }
        inGameHud.render(context, tickCounter);

    }

    @Unique
    private Shader shader;

    @Override
    public void loadShader(Shader shader) {
        if (shader != null && !shader.isLoaded()) {
            shader.init();
        }
        this.shader = shader;
    }

    @Inject(method = "onResized", at = @At("HEAD"))
    private void onResized(int width, int height, CallbackInfo ci) {
        if (shader != null) {
            shader.getFramebuffer().resize(width, height);
        }
    }

    @Inject(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V",
            shift = At.Shift.AFTER))
    private void onRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if (shader != null) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            shader.applyUniforms();
            shader.getShaderEffect().render(client.getFramebuffer(), pool);
        }
    }

    @Redirect(method = "findCrosshairTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"))
    private EntityHitResult onRayTraceDistance(Entity entity, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate, double distance) {
        return ProjectileUtil.raycast(entity, vec3d, vec3d2, box, predicate, GameMap.INSTANCE.get(GameKeys.BYPASS_REACH_LIMIT, false) ? 0d : distance);
    }

    @Redirect(method = "findCrosshairTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D", ordinal = 1))
    private double onDistance(Vec3d self, Vec3d vec3d) {
        return GameMap.INSTANCE.get(GameKeys.BYPASS_REACH_LIMIT, false) ? 2D : self.squaredDistanceTo(vec3d);
    }

    @Override
    public float getFovMultiplier() {
        return fovMultiplier;
    }

    @Override
    public void updateFovMultiplier(float newFov) {
        fovMultiplier = lastFovMultiplier = newFov;
    }

}
