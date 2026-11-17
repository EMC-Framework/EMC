package me.deftware.client.framework.item;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.registry.Identifiable;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

public interface Enchantment extends Identifiable {

    int getMinLevel();

    int getMaxLevel();

    int getProtection(int level);

    float getDamage(int level);

    Message getName(int level);

    /* Internal */

    ResourceKey<net.minecraft.world.item.enchantment.Enchantment> getKey();

    Holder<net.minecraft.world.item.enchantment.Enchantment> getEntry();

}
