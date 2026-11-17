package me.deftware.client.framework.world.block;

import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CartographyTableBlock;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.LoomBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import java.util.function.Predicate;

public enum BlockTypes {

    AIR(block -> block instanceof AirBlock),
    FLUID(block -> block instanceof LiquidBlock),
    CROPS(block -> block instanceof CropBlock),
    ShulkerBox(block -> block instanceof ShulkerBoxBlock),
    Storage(block -> block instanceof ChestBlock || block instanceof BarrelBlock || block instanceof EnderChestBlock),
    Interactable(block -> block instanceof BaseEntityBlock || block instanceof AnvilBlock ||
            block instanceof BedBlock || block instanceof CakeBlock || block instanceof CartographyTableBlock ||
            block instanceof CauldronBlock || block instanceof DiodeBlock || block instanceof ComposterBlock ||
            block instanceof CraftingTableBlock || block instanceof DoorBlock || block instanceof DragonEggBlock ||
            block instanceof FenceBlock || block instanceof FenceGateBlock || block instanceof FlowerPotBlock ||
            block instanceof GrindstoneBlock || block instanceof JigsawBlock || block instanceof LeverBlock ||
            block instanceof LoomBlock || block instanceof NoteBlock || block instanceof RespawnAnchorBlock ||
            block instanceof StonecutterBlock || block instanceof TrapDoorBlock);

    private final Predicate<Block> predicate;

    BlockTypes(Predicate<Block> predicate) {
        this.predicate = predicate;
    }

    public boolean is(Block block) {
        return predicate.test(block);
    }

}
