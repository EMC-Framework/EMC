package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.item.ItemStack;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IInventory.class)
public interface MixinInventory extends me.deftware.client.framework.inventory.Inventory {

    @Unique
    @Override
    default int getSize() {
        return ((IInventory) this).getSizeInventory();
    }

    @Unique
    @Override
    default boolean isEmpty() {
        return ((IInventory) this).isEmpty();
    }

    @Unique
    @Override
    default ItemStack getStackInSlot(int slotId) {
        if (slotId >= getSize())
            return ItemStack.EMPTY;
        return (ItemStack) ((IInventory) this).getStackInSlot(slotId);
    }

}
