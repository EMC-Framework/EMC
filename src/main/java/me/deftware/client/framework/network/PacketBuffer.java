package me.deftware.client.framework.network;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.DoubleBlockPosition;

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

    public void writeItemStack(ItemStack stack) {
        buffer.writeItemStack(stack.getMinecraftItemStack());
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
        return buffer.readString(0);
    }

    public ItemStack readItemStack() {
        return new ItemStack(buffer.readItemStack());
    }

    public BlockPosition readBlockPos() {
        return DoubleBlockPosition.fromMinecraftBlockPos(buffer.readBlockPos());
    }

}
