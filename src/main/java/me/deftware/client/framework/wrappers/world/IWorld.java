package me.deftware.client.framework.wrappers.world;

import me.deftware.client.framework.wrappers.entity.IEntity;
import me.deftware.client.framework.wrappers.entity.IEntityPlayer;
import me.deftware.client.framework.wrappers.entity.ITileEntity;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.world.blocks.IBlockCrops;
import me.deftware.client.framework.wrappers.world.blocks.IBlockNetherWart;
import me.deftware.mixin.imp.IMixinWorld;
import me.deftware.mixin.imp.IMixinWorldClient;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class IWorld {

    private final World world;

    public IWorld(World world) {
        this.world = world;
    }

    public static HashMap<Integer, IEntity> getLoadedEntities() {
        return ((IMixinWorldClient) Minecraft.getMinecraft().theWorld).getIEntities();
    }

    public static Collection<ITileEntity> getLoadedTileEntities() {
        return ((IMixinWorld) Minecraft.getMinecraft().theWorld).getEmcTileEntities();
    }

    public static ArrayList<IBlock> getLoadedBlocks() {
        ArrayList<IBlock> blocks = new ArrayList<>();

        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            Chunk currentChunk = Minecraft.getMinecraft().theWorld.getChunkFromBlockCoords(Minecraft.getMinecraft().thePlayer.getPosition());
            for (BlockPos pos : currentChunk.getTileEntityMap().keySet()) {
                blocks.add(new IBlock(currentChunk.getBlockState(pos).getBlock(), pos));
            }
        }

        return blocks;
    }

    public static ArrayList<IBlock> getLoadedBlocks(int rangeFromPlayer) {
        ArrayList<IBlock> blocks = new ArrayList<>();

        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            for (int xRange = -rangeFromPlayer; xRange <= rangeFromPlayer; xRange++) {
                for (int yRange = -rangeFromPlayer; yRange <= rangeFromPlayer; yRange++) {
                    for (int zRange = -rangeFromPlayer; zRange <= rangeFromPlayer; zRange++) {
                        int posX = (int) (IEntityPlayer.getPosX() + xRange);
                        int posY = (int) MathHelper.clamp_double(IEntityPlayer.getPosY() + yRange, 0, 256);
                        int posZ = (int) (IEntityPlayer.getPosZ() + zRange);

                        IBlockPos pos = new IBlockPos(posX, posY, posZ);
                        IBlock block = IWorld.getBlockFromPos(pos);

                        blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

    public static long getWorldTime() {
        if (Minecraft.getMinecraft().theWorld == null) {
            return 0L;
        }
        return Minecraft.getMinecraft().theWorld.getWorldTime();
    }

    public static void sendQuittingPacket() {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
        }
    }

    public static void leaveWorld() {
        Minecraft.getMinecraft().loadWorld(null);
    }

    public static IBlock getBlockFromPos(IBlockPos pos) {
        Block mBlock = Minecraft.getMinecraft().theWorld.getBlockState(pos.getPos()).getBlock();
        IBlock block = new IBlock(mBlock, pos.getPos());
        if (block.instanceOf(IBlock.IBlockTypes.BlockCrops)) {
            block = new IBlockCrops(mBlock, pos.getPos());
        } else if (block.instanceOf(IBlock.IBlockTypes.BlockNetherWart)) {
            block = new IBlockNetherWart(mBlock, pos.getPos());
        }
        return block;
    }

    public static IBlockState getStateFromPos(IBlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos.getPos());
    }

    public static boolean isNull() {
        return Minecraft.getMinecraft().theWorld == null;
    }

    public int getActualHeight() {
        return world.getActualHeight();
    }

    public IChunk getChunk(IBlockPos pos) {
        return new IChunk(world.getChunkFromBlockCoords(pos.getPos()));
    }

    public World getWorld() {
        return world;
    }

    public boolean containsAnyLiquid(IAxisAlignedBB aabb) {
        return world.isAnyLiquid(aabb.getAABB());
    }


}
