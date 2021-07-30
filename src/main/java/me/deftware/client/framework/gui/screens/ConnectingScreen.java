package me.deftware.client.framework.gui.screens;

import me.deftware.client.framework.minecraft.ServerDetails;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConnecting;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;

/**
 * @author Deftware
 * @since 17.0.0
 */
public interface ConnectingScreen {

	/**
	 * Connects to a Minecraft server
	 *
	 * @param server Server details
	 */
	static void _connect(ServerDetails server) {
		if (server != null) {
			Minecraft.getInstance().displayGuiScreen(new GuiConnecting(
					new GuiMultiplayer(
							Minecraft.getInstance().currentScreen
					), Minecraft.getInstance(), (ServerData) server
			));
		}
	}

}
