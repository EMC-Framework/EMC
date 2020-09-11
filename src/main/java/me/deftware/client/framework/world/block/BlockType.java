package me.deftware.client.framework.world.block;

import net.minecraft.block.*;

/**
 * @author Deftware
 */
public enum BlockType {

	// Types
	BlockContainer, BlockCrops, FluidBlock,

	// Specific blocks
	BlockPumpkin, BlockMelon, BlockReed, BlockCactus, BlockNetherWart, BlockFarmland, BlockSoulSand;

	public boolean instanceOf(Block emcBlock) {
		net.minecraft.block.Block block = emcBlock.getMinecraftBlock();
		if (this.equals(BlockContainer)) {
			return block instanceof BlockContainer;
		} else if (this.equals(BlockCrops)) {
			return block instanceof BlockCrops;
		} else if (this.equals(BlockPumpkin)) {
			return block instanceof BlockPumpkin;
		} else if (this.equals(BlockMelon)) {
			return block instanceof BlockMelon;
		} else if (this.equals(BlockReed)) {
			return block instanceof BlockReed;
		} else if (this.equals(BlockCactus)) {
			return block instanceof BlockCactus;
		} else if (this.equals(BlockNetherWart)) {
			return block instanceof BlockNetherWart;
		} else if (this.equals(BlockFarmland)) {
			return block instanceof BlockFarmland;
		} else if (this.equals(BlockSoulSand)) {
			return block instanceof BlockSoulSand;
		} else if (this.equals(FluidBlock)) {
			return block instanceof BlockFlowingFluid;
		}
		return false;
	}
	
}
