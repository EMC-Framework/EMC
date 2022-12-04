package me.deftware.client.framework.helper;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

/**
 * @author Deftware
 */
public class RenderHelper {

	private static SimpleOption<Boolean> aoMode = null;

	private static boolean getAoMode() {
		return MinecraftClient.getInstance().options.getAo().getValue();
	}

	private static void setAoMode(boolean mode) {
		MinecraftClient.getInstance().options.getAo().setValue(mode);
	}

	public static void reloadRenderers() {
		if (aoMode == null)
			aoMode = SimpleOption.ofBoolean("dummyAoMode", getAoMode());
		if (Bootstrap.blockProperties.isActive()) {
			aoMode.setValue(getAoMode());
			setAoMode(false);
		} else {
			setAoMode(aoMode.getValue());
		}
		MinecraftClient.getInstance().worldRenderer.reload();
	}

}
