package me.deftware.client.framework.entity;

import net.minecraft.world.InteractionHand;

/**
 * @author Deftware
 */
public enum EntityHand {

    MainHand, OffHand, None;

    public InteractionHand getMinecraftHand() {
        if (this == None) {
            throw new IllegalStateException("Cannot convert " + this.name() + " to Minecraft hand");
        }
        return InteractionHand.values()[this.ordinal()];
    }

    public static EntityHand of(InteractionHand hand) {
        return EntityHand.values()[hand.ordinal()];
    }

}
