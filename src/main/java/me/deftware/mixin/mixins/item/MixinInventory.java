package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.item.ItemStack;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Container.class)
public interface MixinInventory extends me.deftware.client.framework.inventory.Inventory {

    @Unique
    @Override
    default int getSize() {
        return ((Container) this).getContainerSize();
    }

    @Unique
    @Override
    default boolean isEmpty() {
        return ((Container) this).isEmpty();
    }

    @Unique
    @Override
    default ItemStack getStackInSlot(int slotId) {
        if (slotId >= getSize()) {
            return ItemStack.EMPTY;
        }
        return (ItemStack) ((Container) this).getItem(slotId);
    }

}
