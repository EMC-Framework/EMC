package me.deftware.client.framework.helper;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.mixin.imp.IMixinGuiEditSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.util.text.TextComponentString;

import java.util.Objects;

/**
 * @author Deftware
 */
public class SignHelper {

	public static boolean isEditing() {
		return net.minecraft.client.Minecraft.getMinecraft().currentScreen != null && net.minecraft.client.Minecraft.getMinecraft().currentScreen.getClass() == GuiEditSign.class;
	}

	public static int getCurrentLine() {
		return Objects.requireNonNull(((IMixinGuiEditSign) net.minecraft.client.Minecraft.getMinecraft().currentScreen)).getEditLine();
	}

	public static ChatMessage getText(int line) {
		return new ChatMessage().fromText(Objects.requireNonNull((IMixinGuiEditSign) Minecraft.getMinecraft().currentScreen).getTileSign().signText[line]);
	}

	public static void setText(String text, int line) {
		Objects.requireNonNull((IMixinGuiEditSign) net.minecraft.client.Minecraft.getMinecraft().currentScreen).getTileSign().signText[line] = new TextComponentString(text);
	}

	public static void save() {
		Objects.requireNonNull((IMixinGuiEditSign) net.minecraft.client.Minecraft.getMinecraft().currentScreen).getTileSign().markDirty();
	}

}
