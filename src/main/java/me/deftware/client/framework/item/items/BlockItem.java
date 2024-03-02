package me.deftware.client.framework.item.items;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.function.Consumer;


public interface BlockItem extends Item {

    Block getBlock();

    static void getItems(ItemStack stack, Consumer<ItemStack> consumer) {
        CompoundTag entityTag = ((net.minecraft.item.ItemStack) stack).getSubTag("BlockEntityTag");
        if (entityTag != null && entityTag.contains("Items", 9)) {
            ListTag items = entityTag.getList("Items", 10);
            for (int i = 0; i < items.size(); i++) {
                CompoundTag data = items.getCompound(i);
                ItemStack itemStack = (ItemStack) net.minecraft.item.ItemStack.fromTag(data);
                consumer.accept(itemStack);
            }
        }
    }

}
