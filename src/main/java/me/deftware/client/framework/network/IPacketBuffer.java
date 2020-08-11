package me.deftware.client.framework.network;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.wrappers.item.IItemStack;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class IPacketBuffer {

    public PacketBuffer buffer;

    public IPacketBuffer() {
        this.buffer = new PacketBuffer(Unpooled.buffer());
    }

    public IPacketBuffer(PacketBuffer buffer) {
        this.buffer = buffer;
    }

    public static PacketBuffer writeItemStack(PacketBuffer targetBuf, ItemStack p_writeItemStack_1_) {
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

    public static ItemStack readItemStack(PacketBuffer targetBuf) throws IOException {
        if (!targetBuf.readBoolean()) {
            return new ItemStack((Item) null);
        } else {
            int i = targetBuf.readVarIntFromBuffer();
            int j = targetBuf.readByte();
            ItemStack itemStack = new ItemStack(Item.getItemById(i), j);
            itemStack.setTagCompound(targetBuf.readNBTTagCompoundFromBuffer());
            return itemStack;
        }
    }

    public void writeItemStack(IItemStack stack) {
        writeItemStack(buffer, stack.getStack());
    }

    public void writeBlockPos(IBlockPos pos) {
        buffer.writeBlockPos(pos.getPos());
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

    public IItemStack readItemStack() throws IOException {
        return new IItemStack(readItemStack(buffer));
    }

    public IBlockPos readBlockPos() {
        return new IBlockPos(buffer.readBlockPos());
    }

}
