package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.world.block.Block;
import net.minecraft.block.state.BlockState;

import net.minecraft.client.Minecraft;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
public abstract class MixinBlockState implements me.deftware.client.framework.world.block.BlockState {

	@Unique
	@Override
	public Block getBlock() {
		return (Block) ((BlockState) (Object) this).getBlock();
	}

	@Unique
	@Override
	public boolean isPathFindable() {
		return ((BlockState) (Object) this).allowsMovement
				(Minecraft.getInstance().world, BlockPos.ORIGIN, PathType.LAND);
	}

}
