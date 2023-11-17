package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.message.Message;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class MixinEnchantment implements me.deftware.client.framework.item.Enchantment {

    @Unique
    private ResourceLocation resourceLocation;

    @Inject(method = "<init>(ILnet/minecraft/util/ResourceLocation;ILnet/minecraft/enchantment/EnumEnchantmentType;)V", at = @At("RETURN"))
    private void onCreate(int enchID, ResourceLocation enchName, int enchWeight, EnumEnchantmentType enchType, CallbackInfo ci) {
        this.resourceLocation = enchName;
    }

    @Unique
    @Override
    public int getMinLevel() {
        return ((Enchantment) (Object) this).getMinLevel();
    }

    @Unique
    @Override
    public int getMaxLevel() {
        return ((Enchantment) (Object) this).getMaxLevel();
    }

    @Unique
    @Override
    public int getProtection(int level) {
        return ((Enchantment) (Object) this).calcModifierDamage(level, DamageSource.generic);
    }

    @Unique
    @Override
    public float getDamage(int level) {
        return ((Enchantment) (Object) this).calcDamageByCreature(level, EnumCreatureAttribute.UNDEFINED);
    }

    @Unique
    @Override
    public Message getName(int level) {
        return Message.of(((Enchantment) (Object) this).getTranslatedName(level));
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Enchantment) (Object) this).getName();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return resourceLocation.getResourcePath();
    }

}
