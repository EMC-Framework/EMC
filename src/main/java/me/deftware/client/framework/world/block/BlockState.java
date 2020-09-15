package me.deftware.client.framework.world.block;

import me.deftware.client.framework.math.position.BlockPosition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

/**
 * @author Deftware
 */
public class BlockState {

	public BlockPos pos;
	protected final IBlockState blockState;
	protected final Material material;

	public BlockState(IBlockState blockState) {
		this.blockState = blockState;
		this.material = new Material(blockState.getBlock().getMaterial());
	}

	public IBlockState getMinecraftBlockState() {
		return blockState;
	}

	public Block getBlock(BlockPosition position) {
		Block block = Block.newInstance(getMinecraftBlockState().getBlock());
		block.setBlockPosition(position);
		return block;
	}


	public Material getMaterial() {
		return material;
	}

}
