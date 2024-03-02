package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.item.ItemTypes;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Deftware
 */
public class CPacketEditBook extends PacketWrapper {

    public CPacketEditBook(Packet<?> packet) {
        super(packet);
    }

    public CPacketEditBook(int slot, List<String> pages, Optional<String> title) {
        super(new BookUpdateC2SPacket(slot, pages, title));
    }

    private static BookUpdateC2SPacket of(ItemStack book) {
        var nbt = book.getNbt().getList("pages", 9);
        if (nbt instanceof NbtList list) {
            List<String> pages = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                pages.add(list.getString(i));
            }
            return new BookUpdateC2SPacket(0, pages, Optional.empty());
        }
        throw new IllegalArgumentException("ItemStack must be a book with a valid NBT");
    }

    public CPacketEditBook(ItemStack book) {
        super(of(book));
    }

    public static void setContents(ItemStack stack, List<String> pages) {
        if (!ItemTypes.WritableBook.is(stack.getItem())) {
            throw new IllegalArgumentException("The stack must be a writable book!");
        }
        NbtCompound nbt = ((net.minecraft.item.ItemStack) stack).getOrCreateNbt();
        NbtList list = new NbtList();
        for (String page : pages) {
            list.add(NbtString.of(page));
        }
        nbt.put("pages", list);
    }

}
