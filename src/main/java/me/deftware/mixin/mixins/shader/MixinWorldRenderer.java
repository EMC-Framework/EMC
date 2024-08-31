package me.deftware.mixin.mixins.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.render.shader.EntityShader;
import me.deftware.client.framework.render.shader.Shader;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.block.Block;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.Handle;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Lazy;
import net.minecraft.util.profiler.Profiler;
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

    /**
     * Main renderer lambda function
     */
    @Unique
    private static final String MAIN_RENDERER = "method_62214";

    @Unique
    private final Lazy<Boolean> isIrisLoaded = new Lazy<>(() -> {
        return FabricLoader.getInstance().isModLoaded("iris");
    });

    @Unique
    private boolean isShaderSupported() {
        return !isIrisLoaded.get();
    }

    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private DefaultFramebufferSet framebufferSet;

    @Unique
    private void initShaders() {
        if (!isShaderSupported()) return;
        for (EntityShader shader : EntityShader.SHADERS) {
            shader.init(bufferBuilders.getEntityVertexConsumers());
        }
    }

    @Inject(method = "onResized", at = @At("HEAD"))
    private void onResized(int width, int height, CallbackInfo ci) {
        if (!isShaderSupported()) return;
        for (EntityShader shader : EntityShader.SHADERS) {
            shader.resize(width, height);
        }
    }

    @Unique
    private Shader targetShader;

    @Inject(method = "loadEntityOutlinePostProcessor", at = @At("RETURN"))
    private void reload(CallbackInfo ci) {
        initShaders();
    }

    @Unique
    private boolean anyShaderEnabled = false;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(ObjectAllocator objectAllocator, RenderTickCounter tickCounter, boolean bl, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        if (!isShaderSupported()) return;
        this.anyShaderEnabled = EntityShader.SHADERS.stream().anyMatch(EntityShader::isEnabled);
        this.targetShader = null;
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z", opcode = 180))
    private boolean hasOutline(MinecraftClient client, Entity entity) {
        if (anyShaderEnabled)
            return false;
        return client.hasOutline(entity);
    }

    @Inject(method = "getEntityOutlinesFramebuffer", at = @At("HEAD"), cancellable = true)
    private void onGetFramebuffer(CallbackInfoReturnable<Framebuffer> cir) {
        if (targetShader != null && anyShaderEnabled) {
            cir.setReturnValue(targetShader.getFramebuffer().getMinecraftBuffer());
        }
    }

    @Inject(method = MAIN_RENDERER, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", ordinal = 0))
    private void onClear(Fog fog, RenderTickCounter renderTickCounter, Camera camera, Profiler profiler, Matrix4f matrix4f, Matrix4f matrix4f2, Handle handle, Handle handle2, Handle handle3, Handle handle4, boolean bl, Frustum frustum, Handle handle5, CallbackInfo ci) {
        if (!isShaderSupported()) return;
        int buffer = GlStateManager.getBoundFramebuffer();
        for (EntityShader shader : EntityShader.SHADERS) {
            if (!shader.isLoaded()) {
                shader.init(bufferBuilders.getEntityVertexConsumers());
            }
            shader.getFramebuffer().clear();
        }
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
    }

    @Redirect(method = "drawEntityOutlinesFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", opcode = 180))
    private boolean onDrawEntityFramebuffer(WorldRenderer worldRenderer) {
        if (!isShaderSupported()) return false;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        for (EntityShader shader : EntityShader.SHADERS) {
            if (shader.isEnabled()) {
                var buffer = shader.getFramebuffer().getMinecraftBuffer();
                var window = client.getWindow();
                buffer.drawInternal(window.getFramebufferWidth(), window.getFramebufferHeight());
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        return false;
    }

    @Redirect(method = "renderBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", opcode = 180, ordinal = 0))
    private void renderBlocKEntity(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, float tickDelta, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider) {
        int buffer = GlStateManager.getBoundFramebuffer();
        if (anyShaderEnabled && isShaderSupported()) {
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
                        targetShader = shader;
                        vertexConsumerProvider = shader.getOutlineVertexConsumerProvider();
                    }
                }
            }
        }
        blockEntityRenderDispatcher.render(blockEntity, tickDelta, matrix, vertexConsumerProvider);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", opcode = 180))
    private void doRenderEntity(WorldRenderer worldRenderer, Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        int buffer = GlStateManager.getBoundFramebuffer();
        if (anyShaderEnabled && isShaderSupported()) {
            me.deftware.client.framework.entity.Entity emcEntity = null;
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isEnabled()) {
                    if (emcEntity == null)
                        emcEntity = ClientWorld.getClientWorld().getEntityByReference(entity);
                    if (shader.getTargetPredicate().test(emcEntity)) {
                        targetShader = shader;
                        vertexConsumers = shader.getOutlineVertexConsumerProvider();
                    }
                }
            }
        }
        renderEntity(entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
    }

    @Unique
    private FrameGraphBuilder frameGraphBuilder;

    @Redirect(method = "render", at = @At(value = "NEW", target = "()Lnet/minecraft/client/render/FrameGraphBuilder;"))
    private FrameGraphBuilder onRenderMain() {
        var frameGraphBuilder = new FrameGraphBuilder();
        this.frameGraphBuilder = frameGraphBuilder;
        return frameGraphBuilder;
    }

    @Redirect(method = MAIN_RENDERER, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;draw()V", opcode = 180))
    private void onVertexDraw(OutlineVertexConsumerProvider outlineVertexConsumerProvider) {
        if (anyShaderEnabled && isShaderSupported()) {
            int original = GlStateManager.getBoundFramebuffer();
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isEnabled()) {
                    targetShader = shader;
                    shader.getOutlineVertexConsumerProvider().draw();
                }
            }
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, original);
        } else {
            outlineVertexConsumerProvider.draw();
        }
    }

    @Inject(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;renderMain(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/client/render/Frustum;Lnet/minecraft/client/render/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/client/render/Fog;ZZLnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/util/profiler/Profiler;)V",
            shift = At.Shift.AFTER))
    private void onPostRenderMain(ObjectAllocator objectAllocator, RenderTickCounter tickCounter, boolean bl, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        if (!isShaderSupported()) return;
        for (EntityShader shader : EntityShader.SHADERS) {
            if (shader.isEnabled()) {
                var mainBuffer = client.getFramebuffer();
                var frameSet = shader.getFramebufferSet();
                var buffer = shader.getFramebuffer().getMinecraftBuffer();
                var name = Shader.ShaderFramebufferSet.FINAL.getPath();
                var handle = frameGraphBuilder.createObjectNode(name, buffer);
                frameSet.set(Shader.ShaderFramebufferSet.FINAL, handle);
                frameSet.set(DefaultFramebufferSet.MAIN, framebufferSet.mainFramebuffer);
                // Add a pass to set uniforms before the shader renders
                // this allows using different uniforms for the same shader
                // when rendering multiple times per frame
                var pass = frameGraphBuilder.createPass("uniforms");
                pass.setRenderer(shader::applyUniforms);
                pass.markToBeVisited();
                shader.getShaderEffect().render(
                        frameGraphBuilder, mainBuffer.textureWidth, mainBuffer.textureHeight, frameSet
                );
            }
        }
    }

}
