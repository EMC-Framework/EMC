package me.deftware.mixin.mixins.shader;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.render.shader.EntityShader;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
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
    private Minecraft mc;

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

    @Inject(method = "makeEntityOutlineShader", at = @At("RETURN"))
    private void reload(CallbackInfo ci) {
        initShaders();
    }

    @Inject(method = "createBindEntityOutlineFbs", at = @At("HEAD"))
    private void onResized(int width, int height, CallbackInfo ci) {
        for (EntityShader shader : EntityShader.SHADERS) {
            if (shader.getShaderEffect() != null)
                shader.getShaderEffect().createBindFramebuffers(width, height);
        }
    }

    @Unique
    private boolean anyShaderEnabled = false;

    @Inject(method = "renderEntities", at = @At("HEAD"))
    private void onRender(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
        this.anyShaderEnabled = EntityShader.SHADERS.stream().anyMatch(EntityShader::isEnabled);
        for (EntityShader shader : EntityShader.SHADERS) {
            if (shader.getFramebuffer() == null) {
                // Not initialised?
                shader.init();
            }
            shader.getFramebuffer().clear();
        }
        this.mc.getFramebuffer().bindFramebuffer(false);
    }

    @Redirect(method = "renderEntityOutlineFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;isRenderEntityOutlines()Z", opcode = 180))
    private boolean onDrawEntityFramebuffer(WorldRenderer worldRenderer) {
        boolean anyMatch = EntityShader.SHADERS.stream().anyMatch(EntityShader::isRender);
        if (canUseShaders() && anyMatch) {
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isRender()) {
                    shader.getFramebuffer().draw(this.mc.mainWindow.getFramebufferWidth(), this.mc.mainWindow.getFramebufferHeight(), false);
                    shader.setRender(false);
                }
            }
            GlStateManager.disableBlend();
            mc.getFramebuffer().bindFramebuffer(false);
        } else {
            GlStateManager.disableLighting();
        }
        return false;
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;render(Lnet/minecraft/tileentity/TileEntity;FI)V", opcode = 180, ordinal = 0))
    private void renderBlocKEntity(TileEntityRendererDispatcher tileEntityRendererDispatcher, net.minecraft.tileentity.TileEntity tileentityIn, float partialTicks, int destroyStage) {
        tileEntityRendererDispatcher.render(tileentityIn, partialTicks, destroyStage);
        if (canUseShaders() && anyShaderEnabled) {
            Block block = null;
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isEnabled()) {
                    if (block == null) {
                        TileEntity tileEntity = ClientWorld.getClientWorld().getTileEntityByReference(tileentityIn);
                        if (tileEntity == null)
                            break;
                        block = tileEntity.getBlock();
                    }
                    if (shader.getTargetPredicate().test(block)) {
                        shader.setRender(true);
                        shader.getFramebuffer().bind(false);
                        tileEntityRendererDispatcher.render(tileentityIn, partialTicks, destroyStage);
                        mc.getFramebuffer().bindFramebuffer(false);
                    }
                }
            }
        }
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntityStatic(Lnet/minecraft/entity/Entity;FZ)V", opcode = 180, ordinal = 1))
    private void doRenderEntity(RenderManager renderManager, Entity entityIn, float partialTicks, boolean bl) {
        renderManager.renderEntityStatic(entityIn, partialTicks, bl);
        if (canUseShaders() && anyShaderEnabled) {
            me.deftware.client.framework.entity.Entity emcEntity = null;
            for (EntityShader shader : EntityShader.SHADERS) {
                if (shader.isEnabled()) {
                    if (emcEntity == null)
                        emcEntity = ClientWorld.getClientWorld().getEntityByReference(entityIn);
                    if (shader.getTargetPredicate().test(emcEntity)) {
                        shader.setRender(true);
                        shader.getFramebuffer().bind(false);
                        RenderHelper.disableStandardItemLighting();
                        renderManager.setRenderOutlines(true);
                        renderManager.renderEntityStatic(entityIn, partialTicks, bl);
                        renderManager.setRenderOutlines(false);
                        RenderHelper.enableStandardItemLighting();
                    }
                }
            }
            mc.getFramebuffer().bindFramebuffer(false);
        }
    }

    @Inject(method = "renderEntities", at = @At("RETURN"))
    private void onDrawShader(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
        GlStateManager.depthFunc(GL11.GL_ALWAYS);
        GlStateManager.disableFog();
        for (EntityShader shader : EntityShader.SHADERS) {
            if (shader.isRender()) {
                shader.getFramebuffer().bind(false);
                shader.getShaderEffect().render(partialTicks);
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
        this.mc.getFramebuffer().bindFramebuffer(false);
    }

}
