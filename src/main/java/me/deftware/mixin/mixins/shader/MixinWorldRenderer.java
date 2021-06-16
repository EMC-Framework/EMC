package me.deftware.mixin.mixins.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.render.Shader;
import me.deftware.client.framework.world.World;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private boolean canUseShaders() {
        if (!FrameworkConstants.OPTIFINE) {
            return true;
        }
        return FrameworkConstants.CAN_RENDER_SHADER;
    }

    @Unique
    private void initShaders() {
        for (Shader shader : Shader.SHADERS)
            shader.init(MinecraftClient.getInstance());
    }

    @Unique
    private Shader getShader(Object obj) {
        for (Shader shader : Shader.SHADERS) {
            if (shader.isEnabled() && shader.getTargetPredicate().test(obj))
                return shader;
        }
        return null;
    }

    @Inject(method = "loadEntityOutlineShader", at = @At("RETURN"))
    private void reload(CallbackInfo ci) {
        initShaders();
    }

    @Inject(method = "onResized", at = @At("HEAD"))
    private void onResized(int width, int height, CallbackInfo ci) {
        for (Shader shader : Shader.SHADERS)
            if (shader.getShaderEffect() != null)
                shader.getShaderEffect().setupDimensions(width, height);
    }

    @Inject(method = "renderEntities", at = @At("HEAD"))
    private void onRender(Camera camera, VisibleRegion visibleRegion, float tickDelta, CallbackInfo ci) {
        // Clear
        for (Shader shader : Shader.SHADERS) {
            if (shader.getFramebuffer() == null) {
                // Not initialised?
                shader.init(MinecraftClient.getInstance());
            }
            shader.getFramebuffer().clear(MinecraftClient.IS_SYSTEM_MAC);
        }
        this.client.getFramebuffer().beginWrite(false);
    }

    @Redirect(method = "drawEntityOutlinesFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", opcode = 180))
    private boolean onDrawEntityFramebuffer(WorldRenderer worldRenderer) {
        if (canUseShaders()) {
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            for (Shader shader : Shader.SHADERS) {
                    shader.getFramebuffer().drawInternal(this.client.window.getFramebufferWidth(), this.client.window.getFramebufferHeight(), false);
                    shader.setRender(false);
            }
            GlStateManager.disableBlend();
            client.getFramebuffer().beginWrite(false);
        }
        return false;
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FI)V", opcode = 180, ordinal = 0))
    private void renderBlocKEntity(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, float tickDelta, int blockBreakStage) {
        blockEntityRenderDispatcher.render(blockEntity, tickDelta, blockBreakStage);
        if (canUseShaders()) {
            Identifier name = BlockEntityType.getId(blockEntity.getType());
            if (name != null) {
                Optional<Block> block = BlockRegistry.INSTANCE.find(name.getPath());
                if (block.isPresent()) {
                    Shader shader = getShader(block.get());
                    if (shader != null) {
                        shader.setRender(true);
                        shader.getFramebuffer().beginWrite(false);
                        blockEntityRenderDispatcher.render(blockEntity, tickDelta, blockBreakStage);
                        client.getFramebuffer().beginWrite(false);
                    }
                }
            }
        }
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;FZ)V", opcode = 180, ordinal = 0))
    private void doRenderEntity(EntityRenderDispatcher entityRenderDispatcher, Entity entity, float tickDelta, boolean bl) {
        entityRenderDispatcher.render(entity, tickDelta, bl);
        if (canUseShaders()) {
            me.deftware.client.framework.entity.Entity emcEntity = World.getEntityById(entity.getEntityId());
            Shader shader = getShader(emcEntity);
            if (shader != null) {
                shader.setRender(true);
                shader.getFramebuffer().beginWrite(false);
                DiffuseLighting.disable();
                entityRenderDispatcher.setRenderOutlines(true);
                entityRenderDispatcher.render(entity, tickDelta, bl);
                entityRenderDispatcher.setRenderOutlines(false);
                DiffuseLighting.enable();
                client.getFramebuffer().beginWrite(false);
            }
        }
    }

    @Inject(method = "renderEntities", at = @At("RETURN"))
    private void onDrawShader(Camera camera, VisibleRegion visibleRegion, float tickDelta, CallbackInfo ci) {
        GlStateManager.depthFunc(GL11.GL_ALWAYS);
        GlStateManager.disableFog();
        for (Shader shader : Shader.SHADERS) {
            if (shader.isRender()) {
                shader.getFramebuffer().beginWrite(false);
                shader.getShaderEffect().render(tickDelta);
            }
        }
        GlStateManager.depthMask(false);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.enableFog();
        GlStateManager.enableBlend();
        GlStateManager.enableColorMaterial();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.enableDepthTest();
        GlStateManager.enableAlphaTest();
        this.client.getFramebuffer().beginWrite(false);
    }

}
