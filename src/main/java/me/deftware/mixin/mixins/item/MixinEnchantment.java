package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityGroup;
import net.minecraft.registry.Registries;
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
        return ((Enchantment) (Object) this).getProtectionAmount(level,
                MinecraftClient.getInstance().world.getDamageSources().generic()
        );
    }

    @Unique
    @Override
    public float getDamage(int level) {
        return ((Enchantment) (Object) this).getAttackDamage(level, EntityGroup.DEFAULT);
    }

    @Unique
    @Override
    public Message getName(int level) {
        return (Message) ((Enchantment) (Object) this).getName(level);
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Enchantment) (Object) this).getTranslationKey();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return Registries.ENCHANTMENT.getId((Enchantment) (Object) this).getPath();
    }

}
