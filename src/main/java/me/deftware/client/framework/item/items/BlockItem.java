package me.deftware.client.framework.item.items;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.core.component.DataComponents;
import java.util.function.Consumer;

public interface BlockItem extends Item {

    Block getBlock();

    static void getItems(ItemStack stack, Consumer<ItemStack> consumer) {
        var container = ((net.minecraft.world.item.ItemStack) stack).get(DataComponents.CONTAINER);
        if (container != null) {
            container.itemCopies().forEach(s -> consumer.accept((ItemStack) (Object) s));
        }
    }

}
