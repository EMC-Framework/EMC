package me.deftware.client.framework.helper;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.render.Shader;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import me.deftware.mixin.imp.IMixinMinecraft;
import net.minecraft.client.Minecraft;

/**
 * @author Deftware
 */
public class WindowHelper {

	public static int getLimitFramerate() {
		return Minecraft.getInstance().gameSettings.limitFramerate;
	}

	public static void setLimitFramerate(int framerate) {
		Minecraft.getInstance().gameSettings.limitFramerate = framerate;
	}

	public static long getWindowHandle() {
		return Minecraft.getInstance().mainWindow.getHandle();
	}

	public static double getScaleFactor() {
		return Minecraft.getInstance().mainWindow.getGuiScaleFactor();
	}

	public static void setScaleFactor(int factor) {
		Minecraft.getInstance().gameSettings.guiScale = factor;
		Minecraft.getInstance().mainWindow.updateSize();
	}

	public static int getGuiScaleRaw() {
		return Minecraft.getInstance().gameSettings.guiScale;
	}

	public static int getGuiScale() {
		int factor =  Minecraft.getInstance().mainWindow.getScaleFactor(Minecraft.getInstance().gameSettings.guiScale);
		if (factor == 0) {
			factor = 4;
		}
		return factor;
	}

	public static boolean isFocused() {
		return ((IMixinMinecraft) net.minecraft.client.Minecraft.getInstance()).getIsWindowFocused();
	}

	public static boolean isMinimized() {
		return GuiScreen.getDisplayHeight() == 0 && GuiScreen.getDisplayWidth() == 0;
	}

	public static int getFPS() {
		return ((IMixinMinecraft) net.minecraft.client.Minecraft.getInstance()).getFPS();
	}

	public static boolean isDebugInfoShown() {
		return Minecraft.getInstance().gameSettings.showDebugInfo;
	}

	public static void setGamma(double value) {
		Minecraft.getInstance().gameSettings.gammaSetting = value;
	}

	public static double getGamma() {
		return Minecraft.getInstance().gameSettings.gammaSetting;
	}

	@Deprecated
	public static void loadShader(MinecraftIdentifier location) {
		((IMixinEntityRenderer) net.minecraft.client.Minecraft.getInstance().gameRenderer).loadCustomShader(location);
	}

	public static void loadShader(Shader shader) {
		((IMixinEntityRenderer) MinecraftClient.getInstance().gameRenderer).loadShader(shader);
	}

	public static void disableShader() {
		Minecraft.getInstance().addScheduledTask(() -> Minecraft.getInstance().gameRenderer.stopUseShader());
	}

}
