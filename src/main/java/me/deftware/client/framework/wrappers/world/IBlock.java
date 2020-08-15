package me.deftware.client.framework.wrappers.world;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.math.IVoxelShape;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class IBlock {

    protected Block block;

    protected IBlockPos pos;

    public IBlock(String name) {
        block = getBlockFromName(name);
    }

    public IBlock(Block block) {
        this.block = block;
    }

    public IBlock(String name, BlockPos pos) {
        block = getBlockFromName(name);
        this.pos = new IBlockPos(pos);
    }

    public IBlock(Block block, BlockPos pos) {
        this.block = block;
        this.pos = new IBlockPos(pos);
    }

    public IBlockPos getBlockPos() {
        return pos;
    }

    public static boolean isValidBlock(String name) {
        return getBlockFromName(name) != null;
    }

    public static boolean isReplaceable(IBlockPos pos) {
        return IWorld.getStateFromPos(pos).getMaterial().isReplaceable();
    }

    private static Block getBlockFromName(String p_getBlockFromName_0_) {
        ResourceLocation lvt_1_1_ = new ResourceLocation(p_getBlockFromName_0_);
        if (Block.REGISTRY.containsKey(lvt_1_1_)) {
            return Block.REGISTRY.getObject(lvt_1_1_);
        }
        return null;
    }

    public static IVoxelShape makeCuboidShape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new IVoxelShape(new IAxisAlignedBB(x1, y1, z1, x2, y2, z2));
    }

    public boolean isValidBlock() {
        return block != null;
    }

    public boolean isAir() {
        return block == Blocks.AIR;
    }

    public boolean isLiquid() {
        return block == Blocks.WATER || block == Blocks.LAVA || block instanceof BlockLiquid;
    }

    public boolean isCaveAir() {
        return block == Blocks.AIR;
    }

    public Block getBlock() {
        return block;
    }

    public int getID() {
        return Block.REGISTRY.getIDForObject(block);
    }

    public ChatMessage getLocalizedName() {
        return new ChatMessage().fromString(block.getLocalizedName());
    }

    public String getBlockKey() {
        return getTranslationKey().substring("tile.".length());
    }

    public String getTranslationKey() {
        return block.getTranslationKey();
    }

    public boolean instanceOf(IBlockTypes type) {
        if (type.equals(IBlockTypes.BlockContainer)) {
            return block instanceof BlockContainer;
        } else if (type.equals(IBlockTypes.BlockCrops)) {
            return block instanceof BlockCrops;
        } else if (type.equals(IBlockTypes.BlockPumpkin)) {
            return block instanceof BlockPumpkin;
        } else if (type.equals(IBlockTypes.BlockMelon)) {
            return block instanceof BlockMelon;
        } else if (type.equals(IBlockTypes.BlockReed)) {
            return block instanceof BlockReed;
        } else if (type.equals(IBlockTypes.BlockCactus)) {
            return block instanceof BlockCactus;
        } else if (type.equals(IBlockTypes.BlockNetherWart)) {
            return block instanceof BlockNetherWart;
        } else if (type.equals(IBlockTypes.BlockFarmland)) {
            return block instanceof BlockFarmland;
        } else if (type.equals(IBlockTypes.BlockSoulSand)) {
            return block instanceof BlockSoulSand;
        } else if (type.equals(IBlockTypes.FluidBlock)) {
            return block instanceof BlockLiquid;
        }
        return false;
    }

    /*
     * Block specific functions
     */

    public enum IBlockTypes {
        // Types
        BlockContainer, BlockCrops, FluidBlock,

        // Specific blocks
        BlockPumpkin, BlockMelon, BlockReed, BlockCactus, BlockNetherWart, BlockFarmland, BlockSoulSand
    }

}
