package me.deftware.mixin.imp;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;

public interface IMixinAbstractClientPlayer {

    PlayerInfo getPlayerNetworkInfo();

    PlayerSkin getCustomSkinTexture();

}
