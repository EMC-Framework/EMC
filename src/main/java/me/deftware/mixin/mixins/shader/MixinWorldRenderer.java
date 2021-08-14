package me.deftware.mixin.mixins.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.render.shader.EntityShader;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        for (EntityShader shader : EntityShader.SHADERS)
            shader.init();
    }

    @Inject(method = "loadEntityOutlineShader", at = @At("RETURN"))
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

    @Inject(method = "renderEntities", at = @At("HEAD"))
    private void onRender(Camera camera, VisibleRegion visibleRegion, float tickDelta, CallbackInfo ci) {
        this.anyShaderEnabled = EntityShader.SHADERS.stream().anyMatch(EntityShader::isEnabled);
        for (EntityShader shader : EntityShader.SHADERS) {
            if (shader.getFramebuffer() == null) {
                // Not initialised?
                shader.init();
            }
            shader.getFramebuffer().clear();
        }
        this.client.getFramebuffer().beginWrite(false);
    }

    @Redirect(method = "drawEntityOutlinesFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", opcode = 180))
    private boolean onDrawEntityFramebuffer(WorldRenderer worldRenderer) {
        boolean anyMatch = EntityShader.SHADERS.stream().anyMatch(EntityShader::isRender);
        if (canUseShaders() && anyMatch) {
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isRender()) {
                    shader.getFramebuffer().draw(this.client.window.getFramebufferWidth(), this.client.window.getFramebufferHeight(), false);
                    shader.setRender(false);
                }
            }
            GlStateManager.disableBlend();
            client.getFramebuffer().beginWrite(false);
        } else {
            GlStateManager.disableLighting();
        }
        return false;
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FI)V", opcode = 180, ordinal = 0))
    private void renderBlocKEntity(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, float tickDelta, int blockBreakStage) {
        blockEntityRenderDispatcher.render(blockEntity, tickDelta, blockBreakStage);
        if (canUseShaders() && anyShaderEnabled) {
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
                        shader.getFramebuffer().bind(false);
                        blockEntityRenderDispatcher.render(blockEntity, tickDelta, blockBreakStage);
                        client.getFramebuffer().beginWrite(false);
                    }
                }
            }
        }
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isGlowing()Z", opcode = 180))
    private boolean hasOutline(Entity entity) {
        if (canUseShaders())
            return false;
        return entity.isGlowing();
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;FZ)V", opcode = 180, ordinal = 0))
    private void doRenderEntity(EntityRenderDispatcher entityRenderDispatcher, Entity entity, float tickDelta, boolean bl) {
        entityRenderDispatcher.render(entity, tickDelta, bl);
        if (canUseShaders() && anyShaderEnabled) {
            me.deftware.client.framework.entity.Entity emcEntity = null;
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isEnabled()) {
                    if (emcEntity == null)
                        emcEntity = ClientWorld.getClientWorld().getEntityByReference(entity);
                    if (shader.getTargetPredicate().test(emcEntity)) {
                        shader.setRender(true);
                        shader.getFramebuffer().bind(false);
                        DiffuseLighting.disable();
                        entityRenderDispatcher.setRenderOutlines(true);
                        entityRenderDispatcher.render(entity, tickDelta, bl);
                        entityRenderDispatcher.setRenderOutlines(false);
                        DiffuseLighting.enable();
                    }
                }
            }
            client.getFramebuffer().beginWrite(false);
        }
    }

    @Inject(method = "renderEntities", at = @At("RETURN"))
    private void onDrawShader(Camera camera, VisibleRegion visibleRegion, float tickDelta, CallbackInfo ci) {
        GlStateManager.depthFunc(GL11.GL_ALWAYS);
        GlStateManager.disableFog();
        for (EntityShader shader : EntityShader.SHADERS) {
            if (shader.isRender()) {
                shader.getFramebuffer().bind(false);
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
