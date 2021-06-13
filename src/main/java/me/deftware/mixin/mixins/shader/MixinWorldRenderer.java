package me.deftware.mixin.mixins.shader;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.registry.BlockRegistry;
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
import net.minecraft.tileentity.TileEntity;
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

    @Unique
    private Shader getShader(Object obj) {
        for (Shader shader : Shader.SHADERS) {
            if (shader.isEnabled() && shader.getTargetPredicate().test(obj))
                return shader;
        }
        return null;
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

    @Inject(method = "renderEntities", at = @At("HEAD"))
    private void onRender(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
        // Clear
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
        if (canUseShaders()) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            for (Shader shader : Shader.SHADERS) {
                if (shader.isRender()) {
                    shader.getFramebuffer().framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, Minecraft.isRunningOnMac);
                    shader.setRender(false);
                }
            }
            GlStateManager.disableBlend();
            mc.getFramebuffer().bindFramebuffer(false);
        }
        return false;
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;renderTileEntity(Lnet/minecraft/tileentity/TileEntity;FI)V", opcode = 180, ordinal = 0))
    private void renderBlocKEntity(TileEntityRendererDispatcher tileEntityRendererDispatcher, TileEntity tileentityIn, float partialTicks, int destroyStage) {
        tileEntityRendererDispatcher.renderTileEntity(tileentityIn, partialTicks, destroyStage);
        if (canUseShaders()) {
                Optional<Block> block = BlockRegistry.INSTANCE.find(tileentityIn.getBlockType().getUnlocalizedName());
                if (block.isPresent()) {
                    Shader shader = getShader(block.get());
                    if (shader != null) {
                        shader.setRender(true);
                        shader.getFramebuffer().bindFramebuffer(false);
                        tileEntityRendererDispatcher.renderTileEntity(tileentityIn, partialTicks, destroyStage);
                        mc.getFramebuffer().bindFramebuffer(false);
                    }
                }
        }
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntitySimple(Lnet/minecraft/entity/Entity;F)Z", opcode = 180, ordinal = 2))
    private boolean doRenderEntity(RenderManager renderManager, Entity entityIn, float partialTicks) {
        boolean state = renderManager.renderEntitySimple(entityIn, partialTicks);
        if (canUseShaders()) {
            me.deftware.client.framework.entity.Entity emcEntity = World.getEntityById(entityIn.getEntityId());
            Shader shader = getShader(emcEntity);
            if (shader != null) {
                shader.setRender(true);
                shader.getFramebuffer().bindFramebuffer(false);
                RenderHelper.disableStandardItemLighting();
                renderManager.setRenderOutlines(true);
                renderManager.renderEntitySimple(entityIn, partialTicks);
                renderManager.setRenderOutlines(false);
                RenderHelper.enableStandardItemLighting();
                mc.getFramebuffer().bindFramebuffer(false);
            }
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
