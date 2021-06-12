package me.deftware.mixin.mixins.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.deftware.client.framework.event.events.EventShader;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.render.Shader;
import me.deftware.client.framework.world.World;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Shadow protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

    @Shadow @Final private MinecraftClient client;
    @Unique
    private final List<Shader> shaders = new ArrayList<>();

    @Inject(method = "reload(Lnet/minecraft/resource/ResourceManager;)V", at = @At("RETURN"))
    public void reload(ResourceManager manager, CallbackInfo ci) {
        initShaders();
    }

    @Inject(method = "onResized", at = @At("HEAD"))
    private void onResized(int width, int height, CallbackInfo ci) {
        for (Shader shader : shaders)
            shader.getShaderEffect().setupDimensions(width, height);
    }

    @Unique
    private void initShaders() {
        for (Shader shader : shaders)
            shader.getShaderEffect().close();
        shaders.clear();
        new EventShader(shaders).broadcast();
        for (Shader shader : shaders)
            shader.init(MinecraftClient.getInstance(), bufferBuilders.getEntityVertexConsumers());
    }

    @Unique
    private Shader getShader(Object obj) {
        for (Shader shader : shaders) {
            if (shader.isEnabled() && shader.getTargetPredicate().test(obj))
                return shader;
        }
        return null;
    }

    private float tickDelta;
    private Framebuffer targetBuffer;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        this.tickDelta = tickDelta;
        targetBuffer = null;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", opcode = 180, ordinal = 0))
    private boolean onClear(WorldRenderer worldRenderer) {
        for (Shader shader : shaders) {
            shader.getFramebuffer().clear(MinecraftClient.IS_SYSTEM_MAC);
        }
        this.client.getFramebuffer().beginWrite(false);
        return false;
    }

    @Redirect(method = "drawEntityOutlinesFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", opcode = 180))
    private boolean onDrawEntityFramebuffer(WorldRenderer worldRenderer) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        for (Shader shader : shaders) {
            if (shader.isRender()) {
                targetBuffer = shader.getFramebuffer();
                shader.getFramebuffer().draw(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight(), false);
                shader.setRender(false);
            }
        }
        RenderSystem.disableBlend();
        return false;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", opcode = 180, ordinal = 0))
    private void renderBlocKEntity(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, float tickDelta, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider) {
        Identifier name = BlockEntityType.getId(blockEntity.getType());
        if (name != null) {
            Optional<Block> block = BlockRegistry.INSTANCE.find(name.getPath());
            if (block.isPresent()) {
                Shader shader = getShader(block.get());
                if (shader != null) {
                    shader.setRender(true);
                    targetBuffer = shader.getFramebuffer();
                    vertexConsumerProvider = shader.getOutlineVertexConsumerProvider();
                }
            }
        }
        blockEntityRenderDispatcher.render(blockEntity, tickDelta, matrix, vertexConsumerProvider);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", opcode = 180))
    private void doRenderEntity(WorldRenderer worldRenderer, Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        me.deftware.client.framework.entity.Entity emcEntity = World.getEntityById(entity.getId());
        Shader shader = getShader(emcEntity);
        if (shader != null) {
            shader.setRender(true);
            targetBuffer = shader.getFramebuffer();
            vertexConsumers = shader.getOutlineVertexConsumerProvider();
        }
        renderEntity(entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;draw()V", opcode = 180))
    private void onVertexDraw(OutlineVertexConsumerProvider outlineVertexConsumerProvider) {
        for (Shader shader : shaders) {
            if (shader.isRender()) {
                targetBuffer = shader.getFramebuffer();
                shader.getOutlineVertexConsumerProvider().draw();
                shader.getShaderEffect().render(tickDelta);
            }
        }
        this.client.getFramebuffer().beginWrite(false);
    }

    @Inject(method = "getEntityOutlinesFramebuffer", at = @At("HEAD"), cancellable = true)
    private void onGetFramebuffer(CallbackInfoReturnable<Framebuffer> cir) {
        cir.setReturnValue(targetBuffer);
    }

}
