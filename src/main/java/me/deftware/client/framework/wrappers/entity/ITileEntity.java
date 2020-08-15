package me.deftware.client.framework.wrappers.entity;

import me.deftware.client.framework.wrappers.world.IBlockPos;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

import java.awt.*;

public class ITileEntity {

    private IChestType chestType;
    private IBlockPos position;
    private Color color = Color.white;

    public ITileEntity(TileEntity entity) {
        chestType = entity instanceof TileEntityChest
                ? ((TileEntityChest)entity).getChestType() == BlockChest.Type.TRAP ? IChestType.TRAPPED_CHEST : IChestType.CHEST
                : entity instanceof TileEntityEnderChest ? IChestType.ENDER_CHEST : null;
        if (chestType != null) {
            color = chestType.equals(IChestType.TRAPPED_CHEST) ? Color.RED
                    : chestType.equals(IChestType.CHEST) ? Color.ORANGE
                    : chestType.equals(IChestType.ENDER_CHEST) ? Color.BLUE : Color.PINK;
        }
        position = new IBlockPos(entity.getPos());
    }

    public IChestType getChestType() {
        return chestType;
    }

    public IBlockPos getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public boolean isChest() {
        return chestType != null;
    }

    public enum IChestType {
        TRAPPED_CHEST, CHEST, ENDER_CHEST, SHULKER_BOX, BARREL
    }

}
