package me.deftware.client.framework.network;

import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.util.List;

/**
 * @author Deftware
 */
public interface NetworkHandler {

	/**
	 * @return The active network handler
	 */
	static NetworkHandler getNetworkHandler() {
		NetHandlerPlayClient handler = Minecraft.getMinecraft().getConnection();
		if (handler == null)
			return null;
		return (NetworkHandler) handler;
	}

	/**
	 * @return A list of all players on a server
	 */
	List<PlayerEntry> _getPlayerList();

}
