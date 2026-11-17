package me.deftware.client.framework.gui.screens;

import me.deftware.client.framework.minecraft.ServerDetails;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

/**
 * @since 17.0.0
 * @author Deftware
 */
public interface ConnectingScreen {

	/**
	 * Connects to a Minecraft server
	 *
	 * @param server Server details
	 */
	static void _connect(ServerDetails server) {
		if (server != null) {
			ConnectScreen.startConnecting(
					new JoinMultiplayerScreen(
							Minecraft.getInstance().gui.screen()
					), Minecraft.getInstance(), ServerAddress.parseString(server._getAddress()), (ServerData) server, false, null
			);
		}
	}

}
