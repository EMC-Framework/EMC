package me.deftware.client.framework.entity;

import net.minecraft.util.EnumHand;

/**
 * @author Deftware
 */
public enum EntityHand {

    MainHand, OffHand, None;

    public EnumHand getMinecraftHand() {
        if (this == None) {
            throw new IllegalStateException("Cannot convert " + this.name() + " to Minecraft hand");
        }
        return EnumHand.values()[this.ordinal()];
    }

    public static EntityHand of(EnumHand hand) {
        return EntityHand.values()[hand.ordinal()];
    }

}
