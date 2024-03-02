package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.item.ItemTypes;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;

import java.util.List;

/**
 * @author Deftware
 */
public class CPacketEditBook extends PacketWrapper {

    public CPacketEditBook(Packet<?> packet) {
        super(packet);
    }

    public CPacketEditBook(ItemStack book) {
        super(new BookUpdateC2SPacket((net.minecraft.item.ItemStack) book, true, 0)); // TODO: Verify last variable (Main hand)
    }

    public static void setContents(ItemStack stack, List<String> pages) {
        if (!ItemTypes.WritableBook.is(stack.getItem())) {
            throw new IllegalArgumentException("The stack must be a writable book!");
        }
        NbtCompound nbt = ((net.minecraft.item.ItemStack) stack).getOrCreateTag();
        NbtList list = new NbtList();
        for (String page : pages) {
            list.add(NbtString.of(page));
        }
        nbt.put("pages", list);
    }

}
