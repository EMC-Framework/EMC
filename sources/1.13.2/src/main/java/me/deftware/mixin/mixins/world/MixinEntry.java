package me.deftware.mixin.mixins.world;

import me.deftware.mixin.imp.IMixinEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.chunk.RenderChunkCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderChunkCache.Entry.class)
public class MixinEntry implements IMixinEntry {

	@Shadow @Final
	protected IBlockState blockState;

	@Override
	public IBlockState getBlockState() {
		return blockState;
	}

}
