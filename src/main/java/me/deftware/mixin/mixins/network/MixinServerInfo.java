package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.minecraft.ServerDetails;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerData.class)
public class MixinServerInfo implements ServerDetails {

	@Override
	public String _getName() {
		return ((ServerData) (Object) this).serverName;
	}

	@Override
	public String _getAddress() {
		return ((ServerData) (Object) this).serverIP;
	}

	@Override
	public ChatMessage _getMotd() {
		return new ChatMessage().fromString(((ServerData) (Object) this).serverMOTD);
	}

	@Override
	public ChatMessage _getPlayers() {
		return new ChatMessage().fromString(((ServerData) (Object) this).playerList);
	}

	@Override
	public boolean _isOnline() {
		return ((ServerData) (Object) this).pinged;
	}

	@Override
	public boolean _isLan() {
		return ((ServerData) (Object) this).isOnLAN();
	}

}
