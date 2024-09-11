package me.deftware.mixin.mixins.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorItem.class)
public class MixinArmorItem extends MixinItem implements me.deftware.client.framework.item.items.ArmorItem {

    @Unique
    private ArmorMaterial armorMaterial;

    @Unique
    private EquipmentType type;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ArmorMaterial armorMaterial, EquipmentType type, Item.Settings settings, CallbackInfo ci) {
        this.armorMaterial = armorMaterial;
        this.type = type;
    }

    @Unique
    @Override
    public int getDamageReduceAmount() {
        return armorMaterial.defense().getOrDefault(type, 0);
    }

    @Unique
    @Override
    public float getToughness() {
        return armorMaterial.toughness();
    }

    @Unique
    @Override
    public int getTypeOrdinal() {
        return type.getEquipmentSlot().ordinal();
    }

}
