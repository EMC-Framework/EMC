package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {

	@Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
	public void tesselate(IWorldReader extendedBlockView_1, IBakedModel bakedModel_1, IBlockState blockState_1, BlockPos blockPos_1, BufferBuilder bufferBuilder_1, boolean boolean_1, Random random_1, long long_1, CallbackInfoReturnable<Boolean> ci) {
		if (blockState_1.getBlock() instanceof BlockFlowingFluid) {
			ci.setReturnValue(((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLUIDS", true)));
		} else {
			if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(IRegistry.BLOCK.getId(blockState_1.getBlock()), "render"))) {
				boolean doRender = (boolean) SettingsMap.getValue(IRegistry.BLOCK.getId(blockState_1.getBlock()), "render", false);
				if (!doRender) {
					ci.setReturnValue(false);
				}
			}
		}
	}


}
