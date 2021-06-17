package me.deftware.client.framework.helper;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.render.Shader;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import me.deftware.mixin.imp.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;

/**
 * @author Deftware
 */
public class WindowHelper {

	public static int getLimitFramerate() {
		return Minecraft.getMinecraft().gameSettings.limitFramerate;
	}

	public static void setLimitFramerate(int framerate) {
		Minecraft.getMinecraft().gameSettings.limitFramerate = framerate;
	}

	public static long getWindowHandle() {
		return 0L;
	}

	private static ScaledResolution getScaledRes() {
		return new ScaledResolution(Minecraft.getMinecraft());
	}

	public static double getScaleFactor() {
		return getScaledRes().getScaleFactor();
	}

	public static void setScaleFactor(int factor) {
		Minecraft.getMinecraft().gameSettings.guiScale = factor;
	}

	public static int getGuiScaleRaw() {
		return Minecraft.getMinecraft().gameSettings.guiScale;
	}

	public static int getGuiScale() {
		int factor =  Minecraft.getMinecraft().gameSettings.guiScale;
		if (factor == 0) {
			factor = 4;
		}
		return factor;
	}

	public static boolean isFocused() {
		return Display.isActive();
	}

	public static boolean isMinimized() {
		return GuiScreen.getDisplayHeight() == 0 && GuiScreen.getDisplayWidth() == 0;
	}

	public static int getFPS() {
		return ((IMixinMinecraft) net.minecraft.client.Minecraft.getMinecraft()).getFPS();
	}

	public static boolean isDebugInfoShown() {
		return Minecraft.getMinecraft().gameSettings.showDebugInfo;
	}

	public static void setGamma(double value) {
		Minecraft.getMinecraft().gameSettings.gammaSetting = (float) value;
	}

	public static double getGamma() {
		return Minecraft.getMinecraft().gameSettings.gammaSetting;
	}

	@Deprecated
	public static void loadShader(MinecraftIdentifier location) {
		((IMixinEntityRenderer) Minecraft.getMinecraft().entityRenderer).loadCustomShader(location);
	}

	public static void loadShader(Shader shader) {
		((IMixinEntityRenderer) Minecraft.getMinecraft().entityRenderer).loadShader(shader);
	}

	public static void disableShader() {
		Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().entityRenderer.stopUseShader());
	}

}
