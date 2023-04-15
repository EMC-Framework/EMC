package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.message.Message;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Enchantment.class)
public class MixinEnchantment implements me.deftware.client.framework.item.Enchantment {

    @Unique
    @Override
    public int getMinLevel() {
        return ((Enchantment) (Object) this).getMinimumLevel();
    }

    @Unique
    @Override
    public int getMaxLevel() {
        return ((Enchantment) (Object) this).getMaximumLevel();
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
        return Registry.ENCHANTMENT.getId((Enchantment) (Object) this).getPath();
    }

}
