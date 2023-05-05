package me.deftware.client.framework.world.block;

import net.minecraft.block.*;

import java.util.function.Predicate;

public enum BlockTypes {

    AIR(block -> block instanceof BlockAir),
    FLUID(block -> block instanceof BlockFlowingFluid),
    CROPS(block -> block instanceof BlockCrops),
    ShulkerBox(block -> false),
    Storage(block -> block instanceof BlockContainer),
    Interactable(block -> block instanceof BlockButton || block instanceof BlockContainer || block instanceof BlockAnvil ||
            block instanceof BlockBed || block instanceof BlockCake ||
            block instanceof BlockRedstoneDiode || block instanceof BlockRedstoneComparator ||
            block instanceof BlockWorkbench || block instanceof BlockDoor || block instanceof BlockDragonEgg ||
            block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockFlowerPot ||
            block instanceof BlockJukebox || block instanceof BlockLever ||
            block instanceof BlockNote | block instanceof BlockTrapDoor);

    private final Predicate<Block> predicate;

    BlockTypes(Predicate<Block> predicate) {
        this.predicate = predicate;
    }

    public boolean is(Block block) {
        return predicate.test(block);
    }

}
