package me.deftware.mixin.mixins.shader;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.render.Shader;
import me.deftware.client.framework.world.World;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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

@Mixin(RenderGlobal.class)
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
        for (Shader shader : Shader.SHADERS)
            shader.init(Minecraft.getMinecraft());
    }

    @Inject(method = "makeEntityOutlineShader", at = @At("RETURN"))
    private void reload(CallbackInfo ci) {
        initShaders();
    }

    @Inject(method = "createBindEntityOutlineFbs", at = @At("HEAD"))
    private void onResized(int width, int height, CallbackInfo ci) {
        for (Shader shader : Shader.SHADERS) {
            if (shader.getShaderEffect() != null)
                shader.getShaderEffect().createBindFramebuffers(width, height);
        }
    }

    @Unique
    private boolean anyShaderEnabled = false;

    @Inject(method = "renderEntities", at = @At("HEAD"))
    private void onRender(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
        this.anyShaderEnabled = Shader.SHADERS.stream().anyMatch(Shader::isEnabled);
        for (Shader shader : Shader.SHADERS) {
            if (shader.getFramebuffer() == null) {
                // Not initialised?
                shader.init(Minecraft.getMinecraft());
            }
            shader.getFramebuffer().framebufferClear();
        }
        this.mc.getFramebuffer().bindFramebuffer(false);
    }

    @Redirect(method = "renderEntityOutlineFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;isRenderEntityOutlines()Z", opcode = 180))
    private boolean onDrawEntityFramebuffer(RenderGlobal worldRenderer) {
        boolean anyMatch = Shader.SHADERS.stream().anyMatch(Shader::isRender);
        if (canUseShaders() && anyMatch) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            for (Shader shader : Shader.SHADERS) {
                if (shader.isRender()) {
                    shader.getFramebuffer().framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
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

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;renderTileEntity(Lnet/minecraft/tileentity/TileEntity;FI)V", opcode = 180, ordinal = 0))
    private void renderBlocKEntity(TileEntityRendererDispatcher tileEntityRendererDispatcher, net.minecraft.tileentity.TileEntity tileentityIn, float partialTicks, int destroyStage) {
        tileEntityRendererDispatcher.renderTileEntity(tileentityIn, partialTicks, destroyStage);
        if (canUseShaders() && anyShaderEnabled) {
            Block block = null;
            for (Shader shader : Shader.SHADERS) {
                if (shader.isEnabled()) {
                    if (block == null) {
                        TileEntity tileEntity = World.getTileEntityFromEntity(tileentityIn);
                        if (tileEntity == null)
                            break;
                        block = tileEntity.getBlock();
                    }
                    if (shader.getTargetPredicate().test(block)) {
                        shader.setRender(true);
                        shader.getFramebuffer().bindFramebuffer(false);
                        tileEntityRendererDispatcher.renderTileEntity(tileentityIn, partialTicks, destroyStage);
                        mc.getFramebuffer().bindFramebuffer(false);
                    }
                }
            }
        }
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntitySimple(Lnet/minecraft/entity/Entity;F)Z", opcode = 180, ordinal = 2))
    private boolean doRenderEntity(RenderManager renderManager, Entity entityIn, float partialTicks) {
        boolean state = renderManager.renderEntitySimple(entityIn, partialTicks);
        if (canUseShaders() && anyShaderEnabled) {
            me.deftware.client.framework.entity.Entity emcEntity = null;
            for (Shader shader : Shader.SHADERS) {
                if (shader.isEnabled()) {
                    if (emcEntity == null)
                        emcEntity = World.getEntityById(entityIn.getEntityId());
                    if (shader.getTargetPredicate().test(emcEntity)) {
                        shader.setRender(true);
                        shader.getFramebuffer().bindFramebuffer(false);
                        RenderHelper.disableStandardItemLighting();
                        renderManager.setRenderOutlines(true);
                        renderManager.renderEntitySimple(entityIn, partialTicks);
                        renderManager.setRenderOutlines(false);
                        RenderHelper.enableStandardItemLighting();
                    }
                }
            }
            mc.getFramebuffer().bindFramebuffer(false);
        }
        return state;
    }

    @Inject(method = "renderEntities", at = @At("RETURN"))
    private void onDrawShader(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
        GlStateManager.depthFunc(GL11.GL_ALWAYS);
        GlStateManager.disableFog();
        for (Shader shader : Shader.SHADERS) {
            if (shader.isRender()) {
                shader.getFramebuffer().bindFramebuffer(false);
                shader.getShaderEffect().loadShaderGroup(partialTicks);
            }
        }
        GlStateManager.depthMask(false);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.enableFog();
        GlStateManager.enableBlend();
        GlStateManager.enableColorMaterial();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        this.mc.getFramebuffer().bindFramebuffer(false);
    }

}
