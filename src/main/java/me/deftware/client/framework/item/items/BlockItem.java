package me.deftware.client.framework.item.items;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.function.Consumer;


public interface BlockItem extends Item {

    Block getBlock();

    static void getItems(ItemStack stack, Consumer<ItemStack> consumer) {
        NbtCompound entityTag = ((net.minecraft.item.ItemStack) stack).getSubNbt("BlockEntityTag");
        if (entityTag != null && entityTag.contains("Items", 9)) {
            NbtList items = entityTag.getList("Items", 10);
            for (int i = 0; i < items.size(); i++) {
                NbtCompound data = items.getCompound(i);
                ItemStack itemStack = (ItemStack) net.minecraft.item.ItemStack.fromNbt(data);
                consumer.accept(itemStack);
            }
        }
    }

}
