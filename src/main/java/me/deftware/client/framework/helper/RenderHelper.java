package me.deftware.client.framework.helper;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.AoMode;

/**
 * @author Deftware
 */
public class RenderHelper {

	private static AoMode aoMode = null;

	public static AoMode getAoMode() {
		return MinecraftClient.getInstance().options.getAo().getValue();
	}

	public static void setAoMode(AoMode mode) {
		MinecraftClient.getInstance().options.getAo().setValue(mode);
	}

	public static void reloadRenderers() {
		if (aoMode == null)
			aoMode = getAoMode();
		if (Bootstrap.blockProperties.isActive()) {
			aoMode = getAoMode();
			setAoMode(AoMode.OFF);
		} else {
			setAoMode(aoMode);
		}
		MinecraftClient.getInstance().worldRenderer.reload();
	}

}
