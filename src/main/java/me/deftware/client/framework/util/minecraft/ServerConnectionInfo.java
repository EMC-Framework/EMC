package me.deftware.client.framework.util.minecraft;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.client.multiplayer.ServerData;

/**
 * @author Deftware
 */
public class ServerConnectionInfo extends ServerData {

	public ServerConnectionInfo(ServerData server) {
		super(server.serverName, server.serverIP, server.isOnLAN());
		this.serverName = server.serverName;
		this.pinged = server.pinged;
		this.gameVersion = server.gameVersion;
		this.pingToServer = server.pingToServer;
		this.version = server.version;
		this.serverMOTD = server.serverMOTD;
		this.populationInfo = server.populationInfo;
		this.setBase64EncodedIconData(server.getBase64EncodedIconData());
	}

	public ServerConnectionInfo(String name, String ip, boolean isLan) {
		super(name, ip, isLan);
	}

	public ChatMessage getMotdAccessor() {
		return new ChatMessage().fromString(serverMOTD);
	}

	public boolean isPingedAccessor() {
		return pinged;
	}

	public String getIPAccessor() {
		return serverIP;
	}

	public boolean isLanServerAccessor () {
		return isOnLAN();
	}

	public String getServerNameAccessor() {
		return serverName;
	}

	public ChatMessage getGameVersionAccessor() {
		return new ChatMessage().fromString(gameVersion);
	}

	public int getVersionAccessor() {
		return version;
	}

	public ChatMessage getPopulationInfoAccessor() {
		return new ChatMessage().fromString(populationInfo);
	}

	public long getPingToServerAccessor() {
		return pingToServer;
	}

	public String getBase64EncodedIconDataAccessor() {
		return getBase64EncodedIconData();
	}

	public void setBase64EncodedIconDataAccessor(String icon) {
		setBase64EncodedIconData(icon);
	}

}
