package me.deftware.mixin.mixins.game;

import me.deftware.client.framework.minecraft.ClientOptions;
import net.minecraft.client.GameSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameSettings.class)
public class MixinGameOptions implements ClientOptions {

	@Override
	public int getViewDistance() {
		return ((GameSettings) (Object) this).renderDistanceChunks;
	}

	@Override
	public boolean isFullScreen() {
		return ((GameSettings) (Object) this).fullScreen;
	}

}
