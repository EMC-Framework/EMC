package me.deftware.client.framework.item;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.registry.Identifiable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public interface Enchantment extends Identifiable {

    int getMinLevel();

    int getMaxLevel();

    int getProtection(int level);

    float getDamage(int level);

    Message getName(int level);

    /* Internal */

    RegistryKey<net.minecraft.enchantment.Enchantment> getKey();

    RegistryEntry<net.minecraft.enchantment.Enchantment> getEntry();

}
