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
        for (int i = 0; i < ((IInventory) this).getSizeInventory(); i++) {
            net.minecraft.item.ItemStack stack = ((IInventory) this).getStackInSlot(i);
            if (stack.getItem() != null) {
                return false;
            }
        }
        return true;
    }

    @Unique
    @Override
    default ItemStack getStackInSlot(int slotId) {
        if (slotId >= getSize())
            return ItemStack.EMPTY;
        ItemStack stack = (ItemStack) ((IInventory) this).getStackInSlot(slotId);
        if (stack == null) {
            return ItemStack.EMPTY;
        }
        return stack;
    }

}
