package me.deftware.client.framework.item.items;

import me.deftware.client.framework.item.Item;

public interface ArmorItem extends Item {

    int getDamageReduceAmount();

    float getToughness();

    int getTypeOrdinal();

}
