package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.world.chunk.BlockClassifier;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkCache.class)
public abstract class MixinWorldChunk {

	/*
			This function is called every time a chunk is built
	*/
	@Redirect(method = "getBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getBlockState(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/block/state/IBlockState;", opcode = 180))
	private IBlockState getBlockState(Chunk chunk, BlockPos p_getBlockState_1_) {
		IBlockState blockState = chunk.getBlockState(p_getBlockState_1_);
		Block block = blockState.getBlock();
		String id = Block.blockRegistry.getNameForObject(block).getResourcePath();
		long position = p_getBlockState_1_.toLong();
		BlockClassifier.CLASSIFIERS.forEach(blockClassifier -> {
			if (blockClassifier.isActive())
				blockClassifier.classify(position, id);
		});
		return blockState;
	}

}
