package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

/**
 * @author Deftware
 */
@Mixin(PlayerInfo.class)
public class MixinPlayerListEntry implements PlayerEntry {

	@Override
	public UUID _getProfileID() {
		return ((PlayerInfo) (Object) this).getProfile().id();
	}

	@Override
	public String _getName() {
		return ((PlayerInfo) (Object) this).getProfile().name();
	}

	@Override
	public Message _getDisplayName() {
		return (Message) ((PlayerInfo) (Object) this).getTabListDisplayName();
	}

}
