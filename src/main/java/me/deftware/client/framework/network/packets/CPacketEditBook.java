package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.item.ItemTypes;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumHand;

import java.util.List;

/**
 * @author Deftware
 */
public class CPacketEditBook extends PacketWrapper {

    public CPacketEditBook(Packet<?> packet) {
        super(packet);
    }

    public CPacketEditBook(ItemStack book) {
        super(new net.minecraft.network.play.client.CPacketEditBook((net.minecraft.item.ItemStack) book, true, EnumHand.MAIN_HAND));
    }

    public static void setContents(ItemStack stack, List<String> pages) {
        if (!ItemTypes.WritableBook.is(stack.getItem())) {
            throw new IllegalArgumentException("The stack must be a writable book!");
        }
        NBTTagCompound nbt = ((net.minecraft.item.ItemStack) stack).getOrCreateTag();
        NBTTagList list = new NBTTagList();
        for (String page : pages) {
            list.add(new NBTTagString(page));
        }
        nbt.put("pages", list);
    }

}
