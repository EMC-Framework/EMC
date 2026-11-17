package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.minecraft.ServerDetails;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerData.class)
public class MixinServerInfo implements ServerDetails {

	@Override
	public String _getName() {
		return ((ServerData) (Object) this).name;
	}

	@Override
	public String _getAddress() {
		return ((ServerData) (Object) this).ip;
	}

	@Override
	public Message _getMotd() {
		return (Message) ((ServerData) (Object) this).motd;
	}

	@Override
	public Message _getPlayers() {
		return (Message) ((ServerData) (Object) this).status;
	}

	@Override
	public boolean _isOnline() {
		return ((ServerData) (Object) this).state() == ServerData.State.SUCCESSFUL;
	}

	@Override
	public boolean _isLan() {
		return ((ServerData) (Object) this).isLan();
	}

}
