package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.message.Message;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.util.DamageSource;
import net.minecraft.util.registry.IRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Enchantment.class)
public class MixinEnchantment implements me.deftware.client.framework.item.Enchantment {

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
        return ((Enchantment) (Object) this).calcModifierDamage(level, DamageSource.GENERIC);
    }

    @Unique
    @Override
    public float getDamage(int level) {
        return ((Enchantment) (Object) this).calcDamageByCreature(level, CreatureAttribute.UNDEFINED);
    }

    @Unique
    @Override
    public Message getName(int level) {
        return (Message) ((Enchantment) (Object) this).getDisplayName(level);
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Enchantment) (Object) this).getName();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return IRegistry.ENCHANTMENT.getKey((Enchantment) (Object) this).getPath();
    }

}
