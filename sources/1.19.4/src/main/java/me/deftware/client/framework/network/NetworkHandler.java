package me.deftware.client.framework.network;

import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageSignatureData;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * @author Deftware
 */
public interface NetworkHandler {

	/**
	 * @return The active network handler
	 */
	static NetworkHandler getNetworkHandler() {
		ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
		if (handler == null)
			return null;
		return (NetworkHandler) handler;
	}

	/**
	 * @return A list of all players on a server
	 */
	List<PlayerEntry> _getPlayerList();

	@ApiStatus.Internal
	LastSeenMessagesCollector.LastSeenMessages collect();

	@ApiStatus.Internal
	MessageSignatureData pack(MessageBody body);

}
