package me.deftware.client.framework.input;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URI;

/**
 * @author Deftware
 */
public class Keyboard {

	public static String getKeyName(int glfwCodePoint) {
		String name = org.lwjgl.input.Keyboard.getKeyName(GLFW.fromGLFW.getOrDefault(glfwCodePoint, glfwCodePoint));
		if (!name.equals("NONE")) return name;
		return org.lwjgl.input.Mouse.getButtonName(GLFW.fromGLFW.getOrDefault(glfwCodePoint, glfwCodePoint));
	}

	public static String getClipboardString() {
		try {
			Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
			if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return (String) transferable.getTransferData(DataFlavor.stringFlavor);
			}
		} catch (Exception ignored) { }
		return "null";
	}

	public static void setClipboardString(String copyText) {
		try {
			StringSelection stringselection = new StringSelection(copyText);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
		} catch (Exception ignored) { }
	}

	public static boolean isKeyDown(int key) {
		if (key <= 2) return false;
		return org.lwjgl.input.Keyboard.isKeyDown(GLFW.fromGLFW.getOrDefault(key, key));
	}

	public static void openLink(String url) {
		try {
			Class<?> oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop").invoke((Object)null);
			oclass.getMethod("browse", URI.class).invoke(object, new URI(url));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean isCtrlPressed() {
		return GuiScreen.isCtrlKeyDown();
	}

	public static boolean isShiftPressed() {
		return GuiScreen.isShiftKeyDown();
	}

}
