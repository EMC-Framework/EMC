package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.item.ItemTypes;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WritableBookContentComponent;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.text.RawFilteredPair;

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
        var contents = ((net.minecraft.item.ItemStack) book).get(DataComponentTypes.WRITABLE_BOOK_CONTENT);
        if (contents != null) {
            var pages = contents.pages();
            List<String> list = new ArrayList<>(pages.size());
            for (var page : pages) {
                list.add(page.raw());
            }
            return new BookUpdateC2SPacket(0, list, Optional.empty());
        }
        throw new IllegalArgumentException("ItemStack must be a writable book with valid data");
    }

    public CPacketEditBook(ItemStack book) {
        super(of(book));
    }

    public static void setContents(ItemStack stack, List<String> pages) {
        if (!ItemTypes.WritableBook.is(stack.getItem())) {
            throw new IllegalArgumentException("The stack must be a writable book!");
        }
        var value = new WritableBookContentComponent(pages.stream().map(RawFilteredPair::of).toList());
        ((net.minecraft.item.ItemStack) stack).set(DataComponentTypes.WRITABLE_BOOK_CONTENT, value);
    }

}
