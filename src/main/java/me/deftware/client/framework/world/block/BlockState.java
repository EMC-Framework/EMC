package me.deftware.client.framework.world.block;

import net.minecraft.block.state.IBlockState;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.registry.BlockRegistry;

/**
 * @author Deftware
 */
public class BlockState {

	protected final IBlockState blockState;
	protected final Material material;

	public BlockState(IBlockState blockState) {
		this.blockState = blockState;
		this.material = new Material(blockState.getMaterial());
	}

	public IBlockState getMinecraftBlockState() {
		return blockState;
	}

	public Block getBlock(BlockPosition position) {
		Block block = BlockRegistry.INSTANCE.getBlock(getMinecraftBlockState().getBlock());
		block.setBlockPosition(position);
		return block;
	}

	public Material getMaterial() {
		return material;
	}

}
