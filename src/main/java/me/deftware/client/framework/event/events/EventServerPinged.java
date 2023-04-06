package me.deftware.client.framework.event.events;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.event.Event;

public class EventServerPinged extends Event {

	private Message serverMOTD, playerList, gameVersion;
	private int version;
	private long pingToServer;

	public EventServerPinged(Message serverMOTD, Message playerList, Message gameVersion, int version, long pingToServer) {
		this.serverMOTD = serverMOTD;
		this.playerList = playerList;
		this.gameVersion = gameVersion;
		this.version = version;
		this.pingToServer = pingToServer;
	}

	public Message getServerMOTD() {
		return serverMOTD;
	}

	public Message getPlayerList() {
		return playerList;
	}

	public Message getGameVersion() {
		return gameVersion;
	}

	public int getVersion() {
		return version;
	}

	public long getPingToServer() {
		return pingToServer;
	}

	public void setServerMOTD(Message serverMOTD) {
		this.serverMOTD = serverMOTD;
	}

	public void setPlayerList(Message playerList) {
		this.playerList = playerList;
	}

	public void setGameVersion(Message gameVersion) {
		this.gameVersion = gameVersion;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setPingToServer(long pingToServer) {
		this.pingToServer = pingToServer;
	}

}
