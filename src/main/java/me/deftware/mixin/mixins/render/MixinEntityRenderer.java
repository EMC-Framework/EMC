package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.helper.GlStateHelper;
import me.deftware.client.framework.helper.WindowHelper;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.shader.Shader;
import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    public abstract Matrix4f getBasicProjectionMatrix(Camera camera, float f, boolean bl);

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    private ShaderEffect shader;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private boolean shadersEnabled;

    @Unique
    private final Consumer<Float> renderEvent = partialTicks -> new EventRender3D(partialTicks).broadcast();

    @Unique
    private final Consumer<Float> renderEventNoBobbing = partialTicks -> new EventRender3DNoBobbing(partialTicks).broadcast();

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHand(MatrixStack matrixStack, Camera camera, float partialTicks, CallbackInfo ci) {
       if (!WindowHelper.isMinimized()) {
           // Normal 3d event
           loadPushPop(renderEvent, matrixStack, partialTicks);
           // Camera model stack without bobbing applied
           MatrixStack matrix = new MatrixStack();
           matrix.push();
           matrix.peek().getModel().multiply(this.getBasicProjectionMatrix(this.camera, partialTicks, true));
           MinecraftClient.getInstance().gameRenderer.loadProjectionMatrix(matrix.peek().getModel());
           // Camera transformation stack
           matrix.pop();
           matrix.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(this.camera.getPitch()));
           matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(this.camera.getYaw() + 180f));
           loadPushPop(renderEventNoBobbing, matrix, partialTicks);
           // Reset projection
           MinecraftClient.getInstance().gameRenderer.loadProjectionMatrix(matrixStack.peek().getModel());
           GlStateHelper.enableLighting();
       }
    }

    @Unique
    private void loadPushPop(Consumer<Float> action, MatrixStack stack, float partialTicks) {
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(stack.peek().getModel());
        action.accept(partialTicks);
        RenderSystem.popMatrix();
    }

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(MatrixStack stack, float partialTicks, CallbackInfo ci) {
        EventHurtcam event = new EventHurtcam();
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", opcode = 180, target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    private void onRender2D(InGameHud inGameHud, MatrixStack matrices, float tickDelta) {
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
            new EventRender2D(tickDelta).broadcast();
            // Render with custom matrix
            RenderStack.reloadCustomMatrix();
            RenderStack.setupGl();
            new EventMatrixRender(tickDelta).broadcast();
            RenderStack.restoreGl();
            RenderStack.reloadMinecraftMatrix();
        }
        inGameHud.render(matrices, tickDelta);
    }

    @Override
    public void loadCustomShader(MinecraftIdentifier location) {
        loadShader(location);
    }

    @Override
    public void loadShader(Shader shader) {
        if (shader == null) {
            this.shader = null;
            this.shadersEnabled = false;
            return;
        }
        if (this.shader != null)
            this.shader.close();
        if (shader.getShaderEffect() == null)
            shader.init();
        else
            shader.getShaderEffect().setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
        this.shader = shader.getShaderEffect();
        this.shadersEnabled = true;
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"))
    private EntityHitResult onRayTraceDistance(Entity entity, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate, double distance) {
        return ProjectileUtil.raycast(entity, vec3d, vec3d2, box, predicate, GameMap.INSTANCE.get(GameKeys.BYPASS_REACH_LIMIT, false) ? 0d : distance);
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D", ordinal = 1))
    private double onDistance(Vec3d self, Vec3d vec3d) {
        return GameMap.INSTANCE.get(GameKeys.BYPASS_REACH_LIMIT, false) ? 2D : self.squaredDistanceTo(vec3d);
    }

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasExtendedReach()Z"))
    private boolean onTest(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        return !GameMap.INSTANCE.get(GameKeys.BYPASS_REACH_LIMIT, false) && clientPlayerInteractionManager.hasExtendedReach();
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
