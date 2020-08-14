package me.deftware.client.framework.event.events;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.Event;
import java.util.List;

/**
 * Triggered by Minecraft server list gui when server is being pinged.
 * This event includes the info about server like: MOTD, IP address, Servers' game varsion, Servers' population info and ping delay
 */
public class EventServerPinged extends Event {
	private ChatMessage serverMOTD;
	private ChatMessage playerList;
	private ChatMessage gameVersion;
	private List<ChatMessage> populationInfo;
	private int version;
	private long pingToServer;

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof EventServerPinged)) return false;
		final EventServerPinged other = (EventServerPinged) o;
		if (!other.canEqual((Object) this)) return false;
		if (!super.equals(o)) return false;
		final Object this$serverMOTD = this.getServerMOTD();
		final Object other$serverMOTD = other.getServerMOTD();
		if (this$serverMOTD == null ? other$serverMOTD != null : !this$serverMOTD.equals(other$serverMOTD)) return false;
		final Object this$playerList = this.getPlayerList();
		final Object other$playerList = other.getPlayerList();
		if (this$playerList == null ? other$playerList != null : !this$playerList.equals(other$playerList)) return false;
		final Object this$gameVersion = this.getGameVersion();
		final Object other$gameVersion = other.getGameVersion();
		if (this$gameVersion == null ? other$gameVersion != null : !this$gameVersion.equals(other$gameVersion)) return false;
		final Object this$populationInfo = this.getPopulationInfo();
		final Object other$populationInfo = other.getPopulationInfo();
		if (this$populationInfo == null ? other$populationInfo != null : !this$populationInfo.equals(other$populationInfo)) return false;
		if (this.getVersion() != other.getVersion()) return false;
		if (this.getPingToServer() != other.getPingToServer()) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof EventServerPinged;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = super.hashCode();
		final Object $serverMOTD = this.getServerMOTD();
		result = result * PRIME + ($serverMOTD == null ? 43 : $serverMOTD.hashCode());
		final Object $playerList = this.getPlayerList();
		result = result * PRIME + ($playerList == null ? 43 : $playerList.hashCode());
		final Object $gameVersion = this.getGameVersion();
		result = result * PRIME + ($gameVersion == null ? 43 : $gameVersion.hashCode());
		final Object $populationInfo = this.getPopulationInfo();
		result = result * PRIME + ($populationInfo == null ? 43 : $populationInfo.hashCode());
		result = result * PRIME + this.getVersion();
		final long $pingToServer = this.getPingToServer();
		result = result * PRIME + (int) ($pingToServer >>> 32 ^ $pingToServer);
		return result;
	}

	public EventServerPinged(final ChatMessage serverMOTD, final ChatMessage playerList, final ChatMessage gameVersion, final List<ChatMessage> populationInfo, final int version, final long pingToServer) {
		this.serverMOTD = serverMOTD;
		this.playerList = playerList;
		this.gameVersion = gameVersion;
		this.populationInfo = populationInfo;
		this.version = version;
		this.pingToServer = pingToServer;
	}

	public ChatMessage getServerMOTD() {
		return this.serverMOTD;
	}

	public ChatMessage getPlayerList() {
		return this.playerList;
	}

	public ChatMessage getGameVersion() {
		return this.gameVersion;
	}

	public List<ChatMessage> getPopulationInfo() {
		return this.populationInfo;
	}

	public int getVersion() {
		return this.version;
	}

	public long getPingToServer() {
		return this.pingToServer;
	}

	public void setServerMOTD(final ChatMessage serverMOTD) {
		this.serverMOTD = serverMOTD;
	}

	public void setPlayerList(final ChatMessage playerList) {
		this.playerList = playerList;
	}

	public void setGameVersion(final ChatMessage gameVersion) {
		this.gameVersion = gameVersion;
	}

	public void setPopulationInfo(final List<ChatMessage> populationInfo) {
		this.populationInfo = populationInfo;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	public void setPingToServer(final long pingToServer) {
		this.pingToServer = pingToServer;
	}

	@Override
	public String toString() {
		return "EventServerPinged(serverMOTD=" + this.getServerMOTD() + ", playerList=" + this.getPlayerList() + ", gameVersion=" + this.getGameVersion() + ", populationInfo=" + this.getPopulationInfo() + ", version=" + this.getVersion() + ", pingToServer=" + this.getPingToServer() + ")";
	}
}
