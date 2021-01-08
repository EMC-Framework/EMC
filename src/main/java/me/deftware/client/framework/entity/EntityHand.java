package me.deftware.client.framework.entity;

import net.minecraft.util.EnumHand;

/**
 * @author Deftware
 */
public enum EntityHand {

    MainHand, OffHand, None;

    public EnumHand getMinecraftHand() {
        return this == MainHand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
    }

}
