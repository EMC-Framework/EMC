package me.deftware.client.framework.helper;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.shader.Shader;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;

/**
 * @author Deftware
 */
public class WindowHelper {

	public static long getWindowHandle() {
		return net.minecraft.client.Minecraft.getInstance().getWindow().handle();
	}

	public static boolean isFocused() {
		return net.minecraft.client.Minecraft.getInstance().isWindowActive();
	}

	public static boolean isMinimized() {
		return GuiScreen.getDisplayHeight() == 0 && GuiScreen.getDisplayWidth() == 0;
	}

	public static int getFPS() {
		return Minecraft.getMinecraftGame().getFPS();
	}

	public static void loadShader(Shader shader) {
		((IMixinEntityRenderer) net.minecraft.client.Minecraft.getInstance().gameRenderer).loadShader(shader);
	}

	public static void disableShader() {
		((IMixinEntityRenderer) net.minecraft.client.Minecraft.getInstance().gameRenderer).loadShader(null);
	}

}
