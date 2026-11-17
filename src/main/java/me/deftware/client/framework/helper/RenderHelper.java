package me.deftware.client.framework.helper;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;

/**
 * @author Deftware
 */
public class RenderHelper {

	private static OptionInstance<Boolean> aoMode = null;

	private static boolean getAoMode() {
		return Minecraft.getInstance().options.ambientOcclusion().get();
	}

	private static void setAoMode(boolean mode) {
		Minecraft.getInstance().options.ambientOcclusion().set(mode);
	}

	public static void reloadRenderers() {
		if (aoMode == null)
			aoMode = OptionInstance.createBoolean("dummyAoMode", getAoMode());
		if (Bootstrap.blockProperties.isActive()) {
			aoMode.set(getAoMode());
			setAoMode(false);
		} else {
			setAoMode(aoMode.get());
		}
		var mc = Minecraft.getInstance();
		if (mc.level != null) {
			mc.levelRenderer.invalidateCompiledGeometry(mc.level, mc.options, mc.gameRenderer.mainCamera(), mc.getBlockColors());
		}
	}

}
