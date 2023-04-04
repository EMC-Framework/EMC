package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.minecraft.ServerDetails;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerInfo.class)
public class MixinServerInfo implements ServerDetails {

	@Override
	public String _getName() {
		return ((ServerInfo) (Object) this).name;
	}

	@Override
	public String _getAddress() {
		return ((ServerInfo) (Object) this).address;
	}

	@Override
	public Message _getMotd() {
		return (Message) ((ServerInfo) (Object) this).label;
	}

	@Override
	public Message _getPlayers() {
		return (Message) ((ServerInfo) (Object) this).playerCountLabel;
	}

	@Override
	public boolean _isOnline() {
		return ((ServerInfo) (Object) this).online;
	}

	@Override
	public boolean _isLan() {
		return ((ServerInfo) (Object) this).isLocal();
	}

}
