package me.deftware.client.framework.item.items;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.function.Consumer;


public interface BlockItem extends Item {

    Block getBlock();

    static void getItems(ItemStack stack, Consumer<ItemStack> consumer) {
        NBTTagCompound entityTag = ((net.minecraft.item.ItemStack) stack).getSubCompound("BlockEntityTag", false);
        if (entityTag != null && entityTag.hasKey("Items", 9)) {
            NBTTagList items = entityTag.getTagList("Items", 10);
            for (int i = 0; i < items.tagCount(); i++) {
                NBTTagCompound data = items.getCompoundTagAt(i);
                ItemStack itemStack = (ItemStack) net.minecraft.item.ItemStack.loadItemStackFromNBT(data);
                consumer.accept(itemStack);
            }
        }
    }

}
