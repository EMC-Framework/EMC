package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.platform.GlStateManager;
import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.render.shader.ShaderTarget;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VisibleRegion;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
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

	@Shadow protected abstract boolean canDrawEntityOutlines();

	@Shadow @Final private MinecraftClient client;

	@Unique
	private ShaderTarget shaderTarget;

	@Unique
	private boolean canDrawCustomBuffers() {
		if (!FrameworkConstants.OPTIFINE) {
			return true;
		}
		return FrameworkConstants.CAN_RENDER_SHADER;
	}

	@Inject(method = "loadEntityOutlineShader", at = @At("HEAD"))
	public void loadEntityOutlineShader(CallbackInfo ci) {
		if (canDrawCustomBuffers())
			Arrays.stream(ShaderTarget.values()).forEach(ShaderTarget::init);
	}

	@Inject(method = "onResized", at = @At("HEAD"))
	public void onResized(int width, int height, CallbackInfo ci) {
		if (canDrawCustomBuffers())
			Arrays.stream(ShaderTarget.values()).forEach(target -> target.onResized(width, height));
	}

	@Redirect(method = "drawEntityOutlinesFramebuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;canDrawEntityOutlines()Z", opcode = 180))
	public boolean drawEntityOutlinesFramebuffer(WorldRenderer worldRenderer) {
		boolean emc = Arrays.stream(ShaderTarget.values()).anyMatch(ShaderTarget::isEnabled);
		if (emc && canDrawCustomBuffers()) {
			GlStateManager.enableBlend();
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
			Arrays.stream(ShaderTarget.values()).forEach(ShaderTarget::renderBuffer);
			GlStateManager.disableBlend();
			return false;
		}
		return canDrawEntityOutlines();
	}

	@Inject(method = "renderEntities", at = @At("HEAD"))
	private void onRenderHead(Camera camera, VisibleRegion visibleRegion, float tickDelta, CallbackInfo ci) {
		if (canDrawCustomBuffers()) {
			Arrays.stream(ShaderTarget.values()).forEach(ShaderTarget::clear);
			client.getFramebuffer().beginWrite(false);
		}
	}

	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FI)V", opcode = 180, ordinal = 0))
	private void renderBlocKEntity(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, float tickDelta, int blockBreakStage) {
		boolean flag;
		if (flag = canDrawCustomBuffers() && ShaderTarget.STORAGE.isEnabled() && (blockEntity instanceof LootableContainerBlockEntity || blockEntity instanceof EnderChestBlockEntity)) {
			shaderTarget = ShaderTarget.STORAGE;
		}
		BlockEntityRenderDispatcher.INSTANCE.render(blockEntity, tickDelta, blockBreakStage);
		if (flag) {
			shaderTarget.getFramebuffer().beginWrite(false);
			BlockEntityRenderDispatcher.INSTANCE.render(blockEntity, tickDelta, blockBreakStage);
			client.getFramebuffer().beginWrite(false);
		}
	}

	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isGlowing()Z", opcode = 180))
	private boolean outlineRedirect(Entity entity) {
		return false;
	}

	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;FZ)V", opcode = 180, ordinal = 0))
	private void doRenderEntity(EntityRenderDispatcher entityRenderDispatcher, Entity entity, float tickDelta, boolean bl) {
		boolean enabled;
		// Player
		if (entity instanceof PlayerEntity) {
			if (enabled = ShaderTarget.PLAYER.isEnabled()) {
				shaderTarget = ShaderTarget.PLAYER;
			}
		} else if (entity instanceof ItemEntity) {
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
		entityRenderDispatcher.render(entity, tickDelta, bl);
		if (enabled) {
			shaderTarget.getFramebuffer().beginWrite(false);
			entityRenderDispatcher.setRenderOutlines(true);
			entityRenderDispatcher.render(entity, tickDelta, bl);
			entityRenderDispatcher.setRenderOutlines(false);
			client.getFramebuffer().beginWrite(false);
		}
	}


}
