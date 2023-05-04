package me.deftware.mixin.mixins.item;

import net.minecraft.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ArmorItem.class)
public class MixinArmorItem extends MixinItem implements me.deftware.client.framework.item.items.ArmorItem {

    @Unique
    @Override
    public int getDamageReduceAmount() {
        return ((ArmorItem) (Object) this).getProtection();
    }

    @Unique
    @Override
    public float getToughness() {
        return ((ArmorItem) (Object) this).getMaterial().getToughness();
    }

    @Unique
    @Override
    public int getTypeOrdinal() {
        return ((ArmorItem) (Object) this).getSlotType().ordinal();
    }

}
