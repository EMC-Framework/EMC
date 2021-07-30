package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

/**
 * @author Deftware
 */
@Mixin(NetworkPlayerInfo.class)
public class MixinPlayerListEntry implements PlayerEntry {

	@Override
	public UUID _getProfileID() {
		return ((NetworkPlayerInfo) (Object) this).getGameProfile().getId();
	}

	@Override
	public String _getName() {
		return ((NetworkPlayerInfo) (Object) this).getGameProfile().getName();
	}

	@Override
	public ChatMessage _getDisplayName() {
		IChatComponent displayName = ((NetworkPlayerInfo) (Object) this).getDisplayName();
		if (displayName != null) {
			return new ChatMessage().fromText(displayName, false);
		}
		return null;
	}

}
