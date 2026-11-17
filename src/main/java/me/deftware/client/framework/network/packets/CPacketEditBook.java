package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.item.ItemTypes;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.component.WritableBookContent;
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
        super(new ServerboundEditBookPacket(slot, pages, title));
    }

    private static ServerboundEditBookPacket of(ItemStack book) {
        var contents = ((net.minecraft.world.item.ItemStack) book).get(DataComponents.WRITABLE_BOOK_CONTENT);
        if (contents != null) {
            var pages = contents.pages();
            List<String> list = new ArrayList<>(pages.size());
            for (var page : pages) {
                list.add(page.raw());
            }
            return new ServerboundEditBookPacket(0, list, Optional.empty());
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
        var value = new WritableBookContent(pages.stream().map(Filterable::passThrough).toList());
        ((net.minecraft.world.item.ItemStack) stack).set(DataComponents.WRITABLE_BOOK_CONTENT, value);
    }

}
