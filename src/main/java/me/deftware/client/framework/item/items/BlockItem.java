package me.deftware.client.framework.item.items;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.component.DataComponentTypes;

import java.util.function.Consumer;

public interface BlockItem extends Item {

    Block getBlock();

    static void getItems(ItemStack stack, Consumer<ItemStack> consumer) {
        var container = ((net.minecraft.item.ItemStack) stack).get(DataComponentTypes.CONTAINER);
        if (container != null) {
            container.stream().forEach(s -> consumer.accept((ItemStack) s));
        }
    }

}
