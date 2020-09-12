package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.entity.types.EntityPlayer;
import me.deftware.client.framework.render.shader.ShaderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    /*
        Shader
     */

	@Shadow protected abstract boolean isRenderEntityOutlines();

	@Shadow @Final private Minecraft mc;

	@Unique
	private ShaderTarget shaderTarget;

	@Unique
	private boolean canDrawCustomBuffers() {
		if (!FrameworkConstants.OPTIFINE) {
			return true;
		}
		return FrameworkConstants.CAN_RENDER_SHADER;
	}

	@Inject(method = "makeEntityOutlineShader", at = @At("HEAD"))
	public void loadEntityOutlineShader(CallbackInfo ci) {
		if (canDrawCustomBuffers())
			Arrays.stream(ShaderTarget.values()).forEach(ShaderTarget::init);
	}

	@Inject(method = "createBindEntityOutlineFbs", at = @At("HEAD"))
	public void onResized(int width, int height, CallbackInfo ci) {
		if (canDrawCustomBuffers())
			Arrays.stream(ShaderTarget.values()).forEach(target -> target.onResized(width, height));
	}

	@Redirect(method = "renderEntityOutlineFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;isRenderEntityOutlines()Z", opcode = 180))
	public boolean drawEntityOutlinesFramebuffer(WorldRenderer worldRenderer) {
		boolean emc = Arrays.stream(ShaderTarget.values()).anyMatch(ShaderTarget::isEnabled);
		if (canDrawCustomBuffers() && emc) {
			GlStateManager.enableBlend();
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
			Arrays.stream(ShaderTarget.values()).forEach(ShaderTarget::renderBuffer);
			GlStateManager.disableBlend();
			return false;
		}
		return isRenderEntityOutlines();
	}

	@Inject(method = "renderEntities", at = @At("HEAD"))
	private void onRenderHead(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
		if (canDrawCustomBuffers()) {
			Arrays.stream(ShaderTarget.values()).forEach(ShaderTarget::clear);
			mc.getFramebuffer().bindFramebuffer(false);
		}
	}

	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;render(Lnet/minecraft/tileentity/TileEntity;FI)V", opcode = 180, ordinal = 0))
	private void renderBlocKEntity(TileEntityRendererDispatcher tileEntityRendererDispatcher, TileEntity blockEntity, float tickDelta, int blockBreakStage) {
		boolean flag;
		if (flag = canDrawCustomBuffers() && ShaderTarget.STORAGE.isEnabled() && (blockEntity instanceof TileEntityLockableLoot || blockEntity instanceof TileEntityEnderChest)) {
			shaderTarget = ShaderTarget.STORAGE;
		}
		tileEntityRendererDispatcher.render(blockEntity, tickDelta, blockBreakStage);
		if (flag) {
			shaderTarget.getFramebuffer().bindFramebuffer(false);
			tileEntityRendererDispatcher.render(blockEntity, tickDelta, blockBreakStage);
			mc.getFramebuffer().bindFramebuffer(false);
		}
	}

	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;isOutlineActive(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;)Z", opcode = 180))
	private boolean outlineRedirect(WorldRenderer worldRenderer, Entity entityIn, Entity viewer, ICamera camera) {
		return false;
	}

	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntityStatic(Lnet/minecraft/entity/Entity;FZ)V", opcode = 180, ordinal = 0))
	private void doRenderEntity(RenderManager renderManager, Entity entity, float partialTicks, boolean bl) {
		boolean enabled;
		// Player
		if (entity instanceof EntityPlayer) {
			if (enabled = ShaderTarget.PLAYER.isEnabled()) {
				shaderTarget = ShaderTarget.PLAYER;
			}
		} else if (entity instanceof EntityItem) {
			if (enabled = ShaderTarget.DROPPED.isEnabled()) {
				shaderTarget = ShaderTarget.DROPPED;
			}
		} else if (enabled = ShaderTarget.ENTITY.isEnabled()) {
			shaderTarget = ShaderTarget.ENTITY;
		}
		if (enabled) enabled = canDrawCustomBuffers();
		// Check target
		if (enabled) {
			enabled = shaderTarget.getPredicate().test(
					new ChatMessage().fromText(entity.getType().getName()).toString(false)
			);
		}
		renderManager.renderEntityStatic(entity, partialTicks, bl);
		if (enabled) {
			shaderTarget.getFramebuffer().bindFramebuffer(false);
			renderManager.setRenderOutlines(true);
			renderManager.renderEntityStatic(entity, partialTicks, bl);
			renderManager.setRenderOutlines(false);
			mc.getFramebuffer().bindFramebuffer(false);
		}
	}


}
