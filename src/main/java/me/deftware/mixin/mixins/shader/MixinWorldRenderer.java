package me.deftware.mixin.mixins.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.render.shader.EntityShader;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private void initShaders() {
        for (EntityShader shader : EntityShader.SHADERS)
            shader.init(bufferBuilders.getEntityVertexConsumers());
    }

    @Unique
    private float tickDelta;

    @Unique
    private Framebuffer targetBuffer;

    @Inject(method = "loadEntityOutlinePostProcessor", at = @At("RETURN"))
    private void reload(CallbackInfo ci) {
        initShaders();
    }

    @Inject(method = "onResized", at = @At("HEAD"))
    private void onResized(int width, int height, CallbackInfo ci) {
        for (EntityShader shader : EntityShader.SHADERS)
            if (shader.getShaderEffect() != null)
                shader.getShaderEffect().setupDimensions(width, height);
    }

    @Unique
    private boolean anyShaderEnabled = false;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        this.anyShaderEnabled = EntityShader.SHADERS.stream().anyMatch(EntityShader::isEnabled);
        this.tickDelta = tickCounter.getTickDelta(true);
        this.targetBuffer = null;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z", opcode = 180))
    private boolean hasOutline(MinecraftClient client, Entity entity) {
        if (anyShaderEnabled)
            return false;
        return client.hasOutline(entity);
    }

    @Inject(method = "getEntityOutlinesFramebuffer", at = @At("HEAD"), cancellable = true)
    private void onGetFramebuffer(CallbackInfoReturnable<Framebuffer> cir) {
        if (targetBuffer != null && anyShaderEnabled)
            cir.setReturnValue(targetBuffer);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", ordinal = 0))
    private void onClear(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        int buffer = GlStateManager.getBoundFramebuffer();
        for (EntityShader shader : EntityShader.SHADERS) {
            if (shader.getFramebuffer() == null) {
                // Not initialised?
                shader.init(bufferBuilders.getEntityVertexConsumers());
            }
            shader.getFramebuffer().clear();
        }
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
    }

    @Redirect(method = "drawEntityOutlinesFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", opcode = 180))
    private boolean onDrawEntityFramebuffer(WorldRenderer worldRenderer) {
        boolean anyMatch = EntityShader.SHADERS.stream().anyMatch(EntityShader::isRender);
        if (anyMatch) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isRender()) {
                    targetBuffer = shader.getFramebuffer().getMinecraftBuffer();
                    shader.getFramebuffer().draw(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight(), false);
                    shader.setRender(false);
                }
            }
            RenderSystem.disableBlend();
        }
        return false;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", opcode = 180, ordinal = 0))
    private void renderBlocKEntity(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, float tickDelta, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider) {
        int buffer = GlStateManager.getBoundFramebuffer();
        if (anyShaderEnabled) {
            Block block = null;
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isEnabled()) {
                    if (block == null) {
                        TileEntity tileEntity = ClientWorld.getClientWorld().getTileEntityByReference(blockEntity);
                        if (tileEntity == null)
                            break;
                        block = tileEntity.getBlock();
                    }
                    if (shader.getTargetPredicate().test(block)) {
                        shader.setRender(true);
                        targetBuffer = shader.getFramebuffer().getMinecraftBuffer();
                        vertexConsumerProvider = shader.getOutlineVertexConsumerProvider();
                    }
                }
            }
        }
        blockEntityRenderDispatcher.render(blockEntity, tickDelta, matrix, vertexConsumerProvider);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", opcode = 180))
    private void doRenderEntity(WorldRenderer worldRenderer, Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        int buffer = GlStateManager.getBoundFramebuffer();
        if (anyShaderEnabled) {
            me.deftware.client.framework.entity.Entity emcEntity = null;
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isEnabled()) {
                    if (emcEntity == null)
                        emcEntity = ClientWorld.getClientWorld().getEntityByReference(entity);
                    if (shader.getTargetPredicate().test(emcEntity)) {
                        shader.setRender(true);
                        targetBuffer = shader.getFramebuffer().getMinecraftBuffer();
                        vertexConsumers = shader.getOutlineVertexConsumerProvider();
                    }
                }
            }
        }
        renderEntity(entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;draw()V", opcode = 180))
    private void onVertexDraw(OutlineVertexConsumerProvider outlineVertexConsumerProvider) {
        if (anyShaderEnabled) {
            int buffer = GlStateManager.getBoundFramebuffer();
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isRender()) {
                    targetBuffer = shader.getFramebuffer().getMinecraftBuffer();
                    shader.getOutlineVertexConsumerProvider().draw();
                    shader.getShaderEffect().render(tickDelta);
                }
            }
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
        } else {
            outlineVertexConsumerProvider.draw();
        }
    }

}
