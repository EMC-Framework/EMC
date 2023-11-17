package me.deftware.mixin.mixins.item;

import net.minecraft.item.ItemArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemArmor.class)
public class MixinArmorItem extends MixinItem implements me.deftware.client.framework.item.items.ArmorItem {

    @Unique
    @Override
    public int getDamageReduceAmount() {
        return ((ItemArmor) (Object) this).damageReduceAmount;
    }

    @Unique
    @Override
    public float getToughness() {
        return ((ItemArmor) (Object) this).getArmorMaterial().getToughness();
    }

    @Unique
    @Override
    public int getTypeOrdinal() {
        return ((ItemArmor) (Object) this).getEquipmentSlot().ordinal();
    }

}
