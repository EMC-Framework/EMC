package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.world.classifier.BlockClassifier;
import me.deftware.mixin.imp.IMixinEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.chunk.RenderChunkCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderChunkCache.class)
public abstract class MixinWorldChunk {

	@Shadow @Final protected RenderChunkCache.Entry[] cache;

	@Shadow protected abstract int getIndex(BlockPos pos);

	/*
				This function is called every time a chunk is built
			 */
	@Inject(method = "getBlockState", at = @At("HEAD"))
	private void getBlockState(BlockPos pos, CallbackInfoReturnable<IBlockState> state) {
		if (cache != null && pos != null) {
			IBlockState blockState = ((IMixinEntry) cache[getIndex(pos)]).getBlockState();
			Block block = blockState.getBlock();
			int id = IRegistry.BLOCK.getId(block);
			BlockClassifier.getClassifiers().forEach(blockClassifier -> blockClassifier.classify(block, pos, id));
		}
	}

}
