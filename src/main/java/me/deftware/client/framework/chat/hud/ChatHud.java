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
		return net.minecraft.client.Minecraft.getInstance().ingameGUI.getChatGUI();
	}

	static IMixinGuiNewChat getMixinHudImpl() {
		/* Internal only! */
		return ((IMixinGuiNewChat) getHud());
	}

	public static void addMessage(ChatMessage message) {
		addMessage(message, 0);
	}

	/**
	 * By specifying a line you can override a message in chat
	 */
	public static void addMessage(ChatMessage message, int line) {
		getMixinHudImpl().setTheChatLine(message.build(), line, net.minecraft.client.Minecraft.getInstance().ingameGUI.getTicks(), false);
	}

	public static List<HudLine> getLines() {
		return getMixinHudImpl().getLines();
	}

	public static void remove(HudLine line) {
		getMixinHudImpl().removeMessage(line);
	}

	public static void remove(int id) {
		getMixinHudImpl().removeLine(id);
	}

	public static Queue<Runnable> getChatMessageQueue() {
		return ChatHud.chatMessageQueue;
	}
}
