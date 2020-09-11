package me.deftware.client.framework.helper;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.AoOption;

/**
 * @author Deftware
 */
public class RenderHelper {

	private static AoOption aoMode = null;

	public static void reloadRenderers() {
		if (aoMode == null) aoMode = MinecraftClient.getInstance().options.ao;
		if (SettingsMap.isOverrideMode()) {
			aoMode = MinecraftClient.getInstance().options.ao;
			MinecraftClient.getInstance().options.ao = AoOption.OFF;
		} else {
			MinecraftClient.getInstance().options.ao = aoMode;
		}
		MinecraftClient.getInstance().worldRenderer.reload();
	}

}
