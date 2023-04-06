package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.message.MessageUtils;
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
	public Message _getMotd() {
		return MessageUtils.parse(((ServerData) (Object) this).serverMOTD);
	}

	@Override
	public Message _getPlayers() {
		return MessageUtils.parse(((ServerData) (Object) this).playerList);
	}

	@Override
	public boolean _isOnline() {
		return ((ServerData) (Object) this).field_78841_f;
	}

	@Override
	public boolean _isLan() {
		return ((ServerData) (Object) this).isOnLAN();
	}

}
