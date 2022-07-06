package me.deftware.client.framework.chat.hud;

import lombok.Getter;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.mixin.imp.IMixinGuiNewChat;
import net.minecraft.client.MinecraftClient;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Deftware
 */
public class ChatHud {

	private @Getter static final Queue<Runnable> chatMessageQueue = new ConcurrentLinkedQueue<>();

	static net.minecraft.client.gui.hud.ChatHud getHud() { /* Internal only! */
		return MinecraftClient.getInstance().inGameHud.getChatHud();
	}

	static IMixinGuiNewChat getMixinHudImpl() { /* Internal only! */
		return ((IMixinGuiNewChat) getHud());
	}

	public static void addMessage(ChatMessage message) {
		addMessage(message, MinecraftClient.getInstance().inGameHud.getTicks());
	}

	/**
	 * By specifying a line you can override a message in chat
	 */
	public static void addMessage(ChatMessage message, int line) {
		getMixinHudImpl().setTheChatLine(message.build(), line, null, false);
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

}
