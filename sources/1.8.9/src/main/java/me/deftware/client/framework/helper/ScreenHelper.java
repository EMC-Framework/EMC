package me.deftware.client.framework.helper;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.util.types.Pair;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * @author Deftware
 */
public class ScreenHelper {

	public static final List<Function<List<String>, List<String>>> debugHudModifiers = new CopyOnWriteArrayList<>();

	public static boolean isScreenOpen() {
		return net.minecraft.client.Minecraft.getMinecraft().currentScreen != null;
	}

	public static boolean isChatOpen() {
		if (net.minecraft.client.Minecraft.getMinecraft().currentScreen != null) {
			return net.minecraft.client.Minecraft.getMinecraft().currentScreen instanceof GuiChat;
		}
		return false;
	}

	public static boolean isContainerOpen() {
		if (net.minecraft.client.Minecraft.getMinecraft().currentScreen != null) {
			return net.minecraft.client.Minecraft.getMinecraft().currentScreen instanceof GuiContainer
					&& !(net.minecraft.client.Minecraft.getMinecraft().currentScreen instanceof GuiInventory);
		}
		return false;
	}

	public static boolean isInventoryOpen() {
		if (net.minecraft.client.Minecraft.getMinecraft().currentScreen != null) {
			return net.minecraft.client.Minecraft.getMinecraft().currentScreen instanceof GuiContainer
					&& (net.minecraft.client.Minecraft.getMinecraft().currentScreen instanceof GuiInventory
					|| net.minecraft.client.Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative);
		}
		return false;
	}

	/**
	 * For internal use only! Does NOT return a compatible {@link GuiScreen} for use in EMC mods!
	 *
	 * @return Returns an instance of a class in the current classpath, however it does NOT return a current instance, but a new one.
	 */
	@SafeVarargs
	@Nullable
	public static net.minecraft.client.gui.GuiScreen createScreenInstance(Object clazz, Pair<Class<?>, Object>... constructorParameters) {
		try {
			Class<?> screenClass = clazz instanceof Class<?> ? (Class<?>) clazz : Class.forName((String) clazz);
			List<Class<?>> paramList = new ArrayList<>();
			List<Object> targetList = new ArrayList<>();
			Arrays.stream(constructorParameters).forEach(c -> {
				paramList.add(c.getLeft());
				targetList.add(c.getRight());
			});
			return (net.minecraft.client.gui.GuiScreen) screenClass.getConstructor(paramList.toArray(new Class<?>[0]))
					.newInstance(targetList.toArray(new Object[0]));
		} catch (Exception ignored) { }
		return null;
	}

}
