package me.deftware.client.framework.helper;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.shader.Shader;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.MinecraftClient;

/**
 * @author Deftware
 */
public class WindowHelper {

	public static long getWindowHandle() {
		return MinecraftClient.getInstance().getWindow().getHandle();
	}

	public static boolean isFocused() {
		return MinecraftClient.getInstance().isWindowFocused();
	}

	public static boolean isMinimized() {
		return GuiScreen.getDisplayHeight() == 0 && GuiScreen.getDisplayWidth() == 0;
	}

	public static int getFPS() {
		return Minecraft.getMinecraftGame().getFPS();
	}

	public static void loadShader(Shader shader) {
		((IMixinEntityRenderer) MinecraftClient.getInstance().gameRenderer).loadShader(shader);
	}

	public static void disableShader() {
		((IMixinEntityRenderer) MinecraftClient.getInstance().gameRenderer).loadShader(null);
	}

}
