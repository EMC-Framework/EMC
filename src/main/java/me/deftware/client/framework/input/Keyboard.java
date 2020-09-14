package me.deftware.client.framework.input;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author Deftware
 */
public class Keyboard {

	public static HashMap<Integer, String> normalKeys = new HashMap<>(),
								functionKeys = new HashMap<>(),
								mouseButtons = new HashMap<>();

	public static void populateCodePoints() {
		for (Field field : GLFW.class.getDeclaredFields()) {
			try {
				if (field.getType() == int.class) {
					String name = field.getName();
					int codePoint = field.getInt(null);
					// Normal keys
					if (name.startsWith("GLFW_KEY_")) {
						if (codePoint >= 32 && codePoint <= 162) {
							normalKeys.put(codePoint, name.substring("GLFW_KEY_".length()));
						} else if (codePoint >= 256 && codePoint <= 348) {
							functionKeys.put(codePoint, name.substring("GLFW_KEY_".length()));
						}
					} else if (name.startsWith("GLFW_MOUSE_")) {
						mouseButtons.put(codePoint, name.substring("GLFW_MOUSE_".length()));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String getKeyName(int glfwCodePoint) {
		if (normalKeys.containsKey(glfwCodePoint)) {
			return normalKeys.get(glfwCodePoint);
		} else if (functionKeys.containsKey(glfwCodePoint)) {
			return functionKeys.get(glfwCodePoint);
		}
		return "Unknown";
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
		// Util.getOSType().openURI(url); FIXME
	}

	public static boolean isCtrlPressed() {
		return GuiScreen.isCtrlKeyDown();
	}

	public static boolean isShiftPressed() {
		return GuiScreen.isShiftKeyDown();
	}

}
