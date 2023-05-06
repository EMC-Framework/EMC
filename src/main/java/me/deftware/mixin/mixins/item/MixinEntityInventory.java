package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.inventory.EntityInventory;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Mixin(InventoryPlayer.class)
public abstract class MixinEntityInventory implements EntityInventory {

    @Unique
    @Override
    public void armor(Consumer<ItemStack> consumer) {
        ((InventoryPlayer) (Object) this).armorInventory.forEach(stack -> consumer.accept((ItemStack) stack));
    }

    @Unique
    @Override
    public void main(Consumer<ItemStack> consumer) {
        ((InventoryPlayer) (Object) this).mainInventory.forEach(stack -> consumer.accept((ItemStack) stack));
    }

    @Unique
    @Override
    public ItemStack getStackInArmourSlot(int slotId) {
        if (slotId >= ((InventoryPlayer) (Object) this).armorInventory.size())
            return ItemStack.EMPTY;
        return (ItemStack) ((InventoryPlayer) (Object) this).armorInventory.get(slotId);
    }

    @Unique
    @Override
    public void setCurrentItem(int id) {
        ((InventoryPlayer) (Object) this).currentItem = id;
    }

    @Unique
    @Override
    public int getCurrentItem() {
        return ((InventoryPlayer) (Object) this).currentItem;
    }

}
