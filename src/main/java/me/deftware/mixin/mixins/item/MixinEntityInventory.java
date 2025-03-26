package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.inventory.EntityInventory;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Mixin(PlayerInventory.class)
public abstract class MixinEntityInventory implements EntityInventory {

    @Unique
    @Override
    public void armor(Consumer<ItemStack> consumer) {
        for (int i = EquipmentSlot.FEET.getIndex(); i <= EquipmentSlot.HEAD.getIndex(); ++i) {
            var item = ((PlayerInventory) (Object) this).getStack(i);
            consumer.accept((ItemStack) item);
        }
    }

    @Unique
    @Override
    public void main(Consumer<ItemStack> consumer) {
        ((PlayerInventory) (Object) this).getMainStacks().forEach(stack -> consumer.accept((ItemStack) stack));
    }

    @Unique
    @Override
    public ItemStack getStackInArmourSlot(int slotId) {
        if (slotId >= 4)
            return ItemStack.EMPTY;
        return (ItemStack) ((PlayerInventory) (Object) this).getStack(slotId + 1);
    }

    @Unique
    @Override
    public void setCurrentItem(int id) {
        ((PlayerInventory) (Object) this).setSelectedSlot(id);
    }

    @Unique
    @Override
    public int getCurrentItem() {
        return ((PlayerInventory) (Object) this).getSelectedSlot();
    }

}
