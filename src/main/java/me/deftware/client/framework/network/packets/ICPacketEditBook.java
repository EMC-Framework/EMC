package me.deftware.client.framework.network.packets;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.network.IPacket;
import me.deftware.client.framework.wrappers.item.IItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class ICPacketEditBook extends IPacket {

    public ICPacketEditBook(Packet<?> packet) {
        super(packet);
    }

    public ICPacketEditBook(IItemStack book) {
        super();

        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        packetbuffer.writeItemStack(book.getStack());

        setPacket(new CPacketCustomPayload("MC|BEdit", packetbuffer));
    }

}