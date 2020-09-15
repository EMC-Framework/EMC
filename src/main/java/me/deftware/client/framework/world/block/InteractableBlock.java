package me.deftware.client.framework.world.block;

import net.minecraft.block.*;

/**
 * @author Deftware
 */
public class InteractableBlock extends Block {

	public InteractableBlock(net.minecraft.block.Block block) {
		super(block);
	}

	public static boolean isInteractable(net.minecraft.block.Block block) {
		return block instanceof BlockButton || block instanceof BlockContainer || block instanceof BlockAnvil ||
				block instanceof BlockBed || block instanceof BlockCake ||
				block instanceof BlockRedstoneDiode || block instanceof BlockRedstoneComparator ||
				block instanceof BlockWorkbench || block instanceof BlockDoor || block instanceof BlockDragonEgg ||
				block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockFlowerPot ||
				block instanceof BlockJukebox || block instanceof BlockLever ||
				block instanceof BlockNote | block instanceof BlockTrapDoor;
	}

}
