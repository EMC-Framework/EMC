package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.item.ItemStack;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Inventory.class)
public interface MixinInventory extends me.deftware.client.framework.inventory.Inventory {

    @Unique
    @Override
    default int getSize() {
        return ((Inventory) this).size();
    }

    @Unique
    @Override
    default boolean isEmpty() {
        return ((Inventory) this).isEmpty();
    }

    @Unique
    @Override
    default ItemStack getStackInSlot(int slotId) {
        if (slotId >= getSize()) {
            return ItemStack.EMPTY;
        }
        return (ItemStack) ((Inventory) this).getStack(slotId);
    }

}
