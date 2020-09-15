package me.deftware.client.framework.network;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.DoubleBlockPosition;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

/**
 * @author Deftware
 */
public class PacketBuffer {

    public net.minecraft.network.PacketBuffer buffer;

    public PacketBuffer() {
        this.buffer = new net.minecraft.network.PacketBuffer(Unpooled.buffer());
    }

    public PacketBuffer(net.minecraft.network.PacketBuffer buffer) {
        this.buffer = buffer;
    }

    public static net.minecraft.network.PacketBuffer writeItemStack(net.minecraft.network.PacketBuffer targetBuf, net.minecraft.item.ItemStack p_writeItemStack_1_) {
        if (p_writeItemStack_1_ == null) {
            targetBuf.writeShort(-1);
        } else {
            targetBuf.writeShort(Item.getIdFromItem(p_writeItemStack_1_.getItem()));
            targetBuf.writeByte(p_writeItemStack_1_.stackSize);
            targetBuf.writeShort(p_writeItemStack_1_.getMetadata());
            NBTTagCompound lvt_2_1_ = null;
            if (p_writeItemStack_1_.getItem().isDamageable() || p_writeItemStack_1_.getItem().getShareTag()) {
                lvt_2_1_ = p_writeItemStack_1_.getTagCompound();
            }

            targetBuf.writeNBTTagCompoundToBuffer(lvt_2_1_);
        }
        return targetBuf;
    }

    public static net.minecraft.item.ItemStack readItemStack(net.minecraft.network.PacketBuffer targetBuf) throws IOException {
        if (!targetBuf.readBoolean()) {
            return new net.minecraft.item.ItemStack((Item) null);
        } else {
            int i = targetBuf.readVarIntFromBuffer();
            int j = targetBuf.readByte();
            net.minecraft.item.ItemStack itemStack = new net.minecraft.item.ItemStack(Item.getItemById(i), j);
            itemStack.setTagCompound(targetBuf.readNBTTagCompoundFromBuffer());
            return itemStack;
        }
    }

    public void writeBlockPos(BlockPosition pos) {
        buffer.writeBlockPos(pos.getMinecraftBlockPos());
    }

    public void writeDouble(double value) {
        buffer.writeDouble(value);
    }

    public void writeFloat(float value) {
        buffer.writeFloat(value);
    }

    public void writeBoolean(boolean value) {
        buffer.writeBoolean(value);
    }

    public void writeString(String value) {
        buffer.writeString(value);
    }

    public double readDouble() {
        return buffer.readDouble();
    }

    public float readFloat() {
        return buffer.readFloat();
    }

    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    public String readString() {
        return buffer.readStringFromBuffer(0);
    }

    public ItemStack readItemStack() {
       try {
           return new ItemStack(readItemStack(buffer));
       } catch (Exception ex) {
           return null;
       }
    }

    public BlockPosition readBlockPos() {
        return DoubleBlockPosition.fromMinecraftBlockPos(buffer.readBlockPos());
    }

}
