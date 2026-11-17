package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.inventory.EntityInventory;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Mixin(Inventory.class)
public abstract class MixinEntityInventory implements EntityInventory {

    @Unique
    @Override
    public void armor(Consumer<ItemStack> consumer) {
        for (int i = 36; i < 40; ++i) {
            var item = ((Inventory) (Object) this).getItem(i);
            consumer.accept((ItemStack) item);
        }
    }

    @Unique
    @Override
    public void main(Consumer<ItemStack> consumer) {
        ((Inventory) (Object) this).getNonEquipmentItems().forEach(stack -> consumer.accept((ItemStack) stack));
    }

    @Unique
    @Override
    public ItemStack getStackInArmourSlot(int slotId) {
        if (slotId < 0 || slotId >= 4)
            return ItemStack.EMPTY;
        return (ItemStack) ((Inventory) (Object) this).getItem(36 + slotId);
    }

    @Unique
    @Override
    public void setCurrentItem(int id) {
        ((Inventory) (Object) this).setSelectedSlot(id);
    }

    @Unique
    @Override
    public int getCurrentItem() {
        return ((Inventory) (Object) this).getSelectedSlot();
    }

}
