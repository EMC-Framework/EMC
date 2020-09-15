package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {

	@Inject(method = "renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/client/renderer/WorldRenderer;Z)Z", at = @At("HEAD"), cancellable = true)
	private void renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer buffer, boolean checkSides, CallbackInfoReturnable<Boolean> ci) {
		if (blockStateIn.getBlock() instanceof BlockLiquid) {
			ci.setReturnValue(((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLUIDS", true)));
		} else {
			if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.blockRegistry.getIDForObject(blockStateIn.getBlock()), "render"))) {
				boolean doRender = (boolean) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockStateIn.getBlock()), "render", false);
				if (!doRender) {
					ci.setReturnValue(false);
				}
			}
		}
	}


}
