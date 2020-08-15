package me.deftware.client.framework.chat.hud;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.mixin.imp.IMixinGuiNewChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Deftware
 */
public class ChatHud {
	private static final Queue<Runnable> chatMessageQueue = new ConcurrentLinkedQueue<>();

	static GuiNewChat getHud() {
		/* Internal only! */
		return Minecraft.getMinecraft().ingameGUI.getChatGUI();
	}

	static IMixinGuiNewChat getMixinHudImpl() {
		/* Internal only! */
		return ((IMixinGuiNewChat) getHud());
	}

	public static void addMessage(ChatMessage message) {
		addMessage(message, 0);
	}

	public static void addMessage(ChatMessage message, int line) {
		getMixinHudImpl().setTheChatLine(message.build(), line, Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), false);
	}

	public static List<ChatHudLine> getLines() {
		return getMixinHudImpl().getLines();
	}

	public static void remove(ChatHudLine line) {
		getMixinHudImpl().removeMessage(line);
	}

	public static void remove(int id) {
		getMixinHudImpl().removeLine(id);
	}

	public static Queue<Runnable> getChatMessageQueue() {
		return ChatHud.chatMessageQueue;
	}
}
