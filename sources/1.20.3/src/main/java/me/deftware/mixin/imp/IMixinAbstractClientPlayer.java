package me.deftware.mixin.imp;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;

public interface IMixinAbstractClientPlayer {

    PlayerListEntry getPlayerNetworkInfo();

    SkinTextures getCustomSkinTexture();

}
