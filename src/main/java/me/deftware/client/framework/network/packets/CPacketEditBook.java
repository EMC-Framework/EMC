package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.item.ItemTypes;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.util.Hand;

import java.util.List;

/**
 * @author Deftware
 */
public class CPacketEditBook extends PacketWrapper {

    public CPacketEditBook(Packet<?> packet) {
        super(packet);
    }

    public CPacketEditBook(ItemStack book) {
        super(new BookUpdateC2SPacket((net.minecraft.item.ItemStack) book, true, Hand.MAIN_HAND));
    }

    public static void setContents(ItemStack stack, List<String> pages) {
        if (!ItemTypes.WritableBook.is(stack.getItem())) {
            throw new IllegalArgumentException("The stack must be a writable book!");
        }
        CompoundTag nbt = ((net.minecraft.item.ItemStack) stack).getOrCreateTag();
        ListTag list = new ListTag();
        for (String page : pages) {
            list.add(StringTag.of(page));
        }
        nbt.put("pages", list);
    }

}
