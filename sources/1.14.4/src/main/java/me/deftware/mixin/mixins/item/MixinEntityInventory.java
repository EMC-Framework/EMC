package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.inventory.EntityInventory;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Mixin(PlayerInventory.class)
public abstract class MixinEntityInventory implements EntityInventory {

    @Unique
    @Override
    public void armor(Consumer<ItemStack> consumer) {
        ((PlayerInventory) (Object) this).armor.forEach(stack -> consumer.accept((ItemStack) stack));
    }

    @Unique
    @Override
    public void main(Consumer<ItemStack> consumer) {
        ((PlayerInventory) (Object) this).main.forEach(stack -> consumer.accept((ItemStack) stack));
    }

    @Unique
    @Override
    public ItemStack getStackInArmourSlot(int slotId) {
        if (slotId >= ((PlayerInventory) (Object) this).armor.size())
            return ItemStack.EMPTY;
        return (ItemStack) ((PlayerInventory) (Object) this).armor.get(slotId);
    }

    @Unique
    @Override
    public void setCurrentItem(int id) {
        ((PlayerInventory) (Object) this).selectedSlot = id;
    }

    @Unique
    @Override
    public int getCurrentItem() {
        return ((PlayerInventory) (Object) this).selectedSlot;
    }

}
