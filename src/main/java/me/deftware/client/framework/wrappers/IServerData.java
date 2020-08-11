package me.deftware.client.framework.wrappers;

import net.minecraft.client.options.ServerEntry;
import me.deftware.client.framework.chat.ChatMessage;

public class IServerData extends ServerEntry {

    public IServerData(String name, String ip, boolean isLan) {
        super(name, ip, isLan);
    }

    public ChatMessage getIMotd() {
        return new ChatMessage().fromString(label);
    }

    public boolean isIPinged() {
        return online;
    }

    public String getIIP() {
        return address;
    }

    public boolean isILanServer() {
        return isLocal();
    }

    public String getIServerName() {
        return name;
    }

    public ChatMessage getIGameVersion() {
        return new ChatMessage().fromString(version);
    }

    public int getIVersion() {
        return protocolVersion;
    }

    public ChatMessage getIPopulationInfo() {
        return new ChatMessage().fromString(playerCountLabel);
    }

    public long getIPingToServer() {
        return ping;
    }

    public String getIBase64EncodedIconData() {
        return getIcon();
    }

    public void setIBase64EncodedIconData(String icon) {
        setIcon(icon);
    }

}
