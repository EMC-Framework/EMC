package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

/**
 * @author Deftware
 */
@Mixin(PlayerListEntry.class)
public class MixinPlayerListEntry implements PlayerEntry {

	@Override
	public UUID _getProfileID() {
		return ((PlayerListEntry) (Object) this).getProfile().getId();
	}

	@Override
	public String _getName() {
		return ((PlayerListEntry) (Object) this).getProfile().getName();
	}

	@Override
	public Message _getDisplayName() {
		return (Message) ((PlayerListEntry) (Object) this).getDisplayName();
	}

}
