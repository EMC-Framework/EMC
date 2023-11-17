package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.world.block.Block;

import net.minecraft.block.state.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockStateBase.class)
public abstract class MixinBlockState implements me.deftware.client.framework.world.block.BlockState {

	@Unique
	@Override
	public Block getBlock() {
		return (Block) ((BlockStateBase) (Object) this).getBlock();
	}

	@Unique
	@Override
	public boolean isPathFindable() {
		return false; // TODO
	}

}
