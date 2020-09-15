package me.deftware.client.framework.network.packets;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

/**
 * @author Deftware
 */
public class CPacketEditBook extends PacketWrapper {

    public CPacketEditBook(Packet<?> packet) {
        super(packet);
    }

    public CPacketEditBook(ItemStack book) {
        super(null);
        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        me.deftware.client.framework.network.PacketBuffer.writeItemStack(packetbuffer, book.getMinecraftItemStack());
        packet = new C17PacketCustomPayload("MC|BEdit", packetbuffer);
    }

}
