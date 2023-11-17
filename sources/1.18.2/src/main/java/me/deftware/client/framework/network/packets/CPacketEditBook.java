package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;

/**
 * @author Deftware
 */
public class CPacketEditBook extends PacketWrapper {

    public CPacketEditBook(Packet<?> packet) {
        super(packet);
    }

    public CPacketEditBook(ItemStack book) {
        super(null); //new BookUpdateC2SPacket(book.getMinecraftItemStack(), true, 0)); // TODO: Verify last variable (Main hand)
    }

}
