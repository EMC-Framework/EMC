package me.deftware.client.framework.world.block;

import net.minecraft.block.*;

import java.util.function.Predicate;

public enum BlockTypes {

    AIR(block -> block instanceof AirBlock),
    FLUID(block -> block instanceof FluidBlock),
    CROPS(block -> block instanceof CropBlock),
    ShulkerBox(block -> block instanceof ShulkerBoxBlock),
    Storage(block -> block instanceof ChestBlock || block instanceof BarrelBlock || block instanceof EnderChestBlock),
    Interactable(block -> block instanceof AbstractButtonBlock || block instanceof BlockWithEntity || block instanceof AnvilBlock ||
            block instanceof BedBlock || block instanceof CakeBlock || block instanceof CartographyTableBlock ||
            block instanceof CauldronBlock || block instanceof AbstractRedstoneGateBlock || block instanceof ComposterBlock ||
            block instanceof CraftingTableBlock || block instanceof DoorBlock || block instanceof DragonEggBlock ||
            block instanceof FenceBlock || block instanceof FenceGateBlock || block instanceof FlowerPotBlock ||
            block instanceof GrindstoneBlock || block instanceof JigsawBlock || block instanceof LeverBlock ||
            block instanceof LoomBlock || block instanceof NoteBlock ||
            block instanceof StonecutterBlock || block instanceof TrapdoorBlock);

    private final Predicate<Block> predicate;

    BlockTypes(Predicate<Block> predicate) {
        this.predicate = predicate;
    }

    public boolean is(Block block) {
        return predicate.test(block);
    }

}
