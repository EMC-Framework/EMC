package me.deftware.client.framework.network.packets;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.network.IPacket;
import me.deftware.client.framework.network.IPacketBuffer;
import me.deftware.client.framework.wrappers.item.IItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ICPacketEditBook extends IPacket {

    public ICPacketEditBook(Packet<?> packet) {
        super(packet);
    }

    public ICPacketEditBook(IItemStack book) {
        super();

        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        IPacketBuffer.writeItemStack(packetbuffer, book.getStack());

        setPacket(new C17PacketCustomPayload("MC|BEdit", packetbuffer));
    }

}