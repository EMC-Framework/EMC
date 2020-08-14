package me.deftware.client.framework.wrappers;

import net.minecraft.client.multiplayer.ServerData;
import me.deftware.client.framework.chat.ChatMessage;

public class IServerData extends ServerData {

    public IServerData(String name, String ip, boolean isLan) {
        super(name, ip, isLan);
    }

    public ChatMessage getIMotd() {
        return new ChatMessage().fromString(serverMOTD);
    }

    public boolean isIPinged() {
        return pinged;
    }

    public String getIIP() {
        return serverIP;
    }

    public boolean isILanServer() {
        return isOnLAN();
    }

    public String getIServerName() {
        return serverName;
    }

    public ChatMessage getIGameVersion() {
        return new ChatMessage().fromString(gameVersion);
    }

    public int getIVersion() {
        return version;
    }

    public ChatMessage getIPopulationInfo() {
        return new ChatMessage().fromString(populationInfo);
    }

    public long getIPingToServer() {
        return pingToServer;
    }

    public String getIBase64EncodedIconData() {
        return getBase64EncodedIconData();
    }

    public void setIBase64EncodedIconData(String icon) {
        setBase64EncodedIconData(icon);
    }

}
