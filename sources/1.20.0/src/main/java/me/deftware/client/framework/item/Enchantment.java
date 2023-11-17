package me.deftware.client.framework.item;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.registry.Identifiable;

public interface Enchantment extends Identifiable {

    int getMinLevel();

    int getMaxLevel();

    int getProtection(int level);

    float getDamage(int level);

    Message getName(int level);

}
