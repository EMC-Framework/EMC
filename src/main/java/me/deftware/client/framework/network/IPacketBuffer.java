package me.deftware.client.framework.network;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.wrappers.item.IItemStack;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import net.minecraft.network.PacketBuffer;

public class IPacketBuffer {

    public PacketBuffer buffer;

    public IPacketBuffer() {
        this.buffer = new PacketBuffer(Unpooled.buffer());
    }

    public IPacketBuffer(PacketBuffer buffer) {
        this.buffer = buffer;
    }

    public void writeItemStack(IItemStack stack) {
        buffer.writeItemStack(stack.getStack());
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
        return buffer.readString(0);
    }

    public IItemStack readItemStack() {
        return new IItemStack(buffer.readItemStack());
    }

    public IBlockPos readBlockPos() {
        return new IBlockPos(buffer.readBlockPos());
    }

}
