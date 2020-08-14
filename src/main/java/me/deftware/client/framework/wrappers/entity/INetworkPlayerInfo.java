package me.deftware.client.framework.wrappers.entity;

import net.minecraft.client.network.NetworkPlayerInfo;

public class INetworkPlayerInfo {

    private NetworkPlayerInfo data;

    public INetworkPlayerInfo(NetworkPlayerInfo data) {
        this.data = data;
    }

    public boolean isNull() {
        return data == null;
    }

    public boolean isSurvivalOrAdventure() {
        return data.getGameType().isSurvivalOrAdventure();
    }

    public boolean isCreative() {
        return data.getGameType().isCreative();
    }

}
