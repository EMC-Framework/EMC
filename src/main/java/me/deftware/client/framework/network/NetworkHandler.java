package me.deftware.client.framework.network;

import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.LastSeenMessagesTracker;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignedMessageBody;
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
		ClientPacketListener handler = Minecraft.getInstance().getConnection();
		if (handler == null)
			return null;
		return (NetworkHandler) handler;
	}

	/**
	 * @return A list of all players on a server
	 */
	List<PlayerEntry> _getPlayerList();

	@ApiStatus.Internal
	LastSeenMessagesTracker.Update collect();

	@ApiStatus.Internal
	MessageSignature pack(SignedMessageBody body);

}
