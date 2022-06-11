package me.deftware.client.framework.chat;

import com.mojang.brigadier.Message;
import lombok.Getter;
import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.chat.style.ChatStyle;
import me.deftware.client.framework.entity.types.main.MainEntityPlayer;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Deftware
 */
public class ChatMessage implements Message {

	private MutableText compiledText; /* Cache */

	@Getter
	protected final List<ChatSection> sectionList = new ArrayList<>();

	public static ChatMessage join(ChatMessage... messages) {
		ChatMessage message = null;
		if (messages.length != 0) {
			message = messages[0];
			if (messages.length > 1) {
				for (int i = 1; i < messages.length; i++) {
					message.join(messages[i]);
				}
			}
		}
		return message;
	}

	@Override
	public String getString() {
		return this.toString();
	}

	public String toString() {
		return this.toString(false);
	}

	public String toString(boolean withFormatting) {
		StringBuilder builder = new StringBuilder();
		for (ChatSection section : sectionList) {
			builder.append(withFormatting ? Formatting.RESET.toString() + section.getStyle().toString() : "").append(section.getText());
		}
		return builder.toString();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof String) {
			return toString(false).equalsIgnoreCase(ChatStyle.stripColors((String) object));
		} else if (object instanceof ChatMessage) {
			return toString(false).equalsIgnoreCase(((ChatMessage) object).toString(false));
		}
		return false;
	}

	public ChatMessage trimToWidth(int width) {
		String text = toString(true);
		if (FontRenderer.getStringWidth(text) > width) {
			StringBuilder builder = new StringBuilder();
			for (String _char : text.split("")) {
				if (FontRenderer.getStringWidth(builder.toString() + _char + "...") < width - 6) {
					builder.append(_char);
				} else {
					builder.append("...");
					break;
				}
			}
			text = builder.toString();
		}
		return new ChatMessage().fromString(text);
	}

	public ChatMessage fromString(String text) {
		return fromString(text, ChatStyle.getFormattingChar());
	}

	/**
	 * Converts an old style formatted string to a {@link ChatMessage} object
	 */
	public ChatMessage fromString(String text, char formattingChar) {
		if (!text.contains(String.valueOf(formattingChar))) {
			// No need to parse if the string doesnt have any formatting
			sectionList.add(new ChatSection(text));
		} else {
			// Split string by formatting char
			String[] sections = text.split(String.valueOf(formattingChar));
			ChatSection currentSection = new ChatSection("");
			for (String section : sections) {
				if (section.length() == 0) continue; // Start of string
				if (section.length() == 1) { // Only formatting, no text
					currentSection.getStyle().fromCode(section);
				} else {
					currentSection.getStyle().fromCode(section.substring(0, 1)); // The first char is always the formatting char
					currentSection.setText(section.substring(1));
					ChatStyle lastStyle = currentSection.getStyle().deepCopy();
					sectionList.add(currentSection);
					currentSection = new ChatSection("");
					currentSection.setStyle(lastStyle);
				}
			}
		}
		return this;
	}

	public ChatMessage replace(String search, String replace) {
		for (ChatSection section : sectionList) {
			section.setText(section.getText().replace(search, replace));
		}
		purgeCache();
		return this;
	}

	public ChatMessage fromText(Text text) {
		// This function is highly dependant on which Minecraft version this is implemented on
		text.visit((style, string) -> {
			ChatSection section = new ChatSection(string);
			section.getStyle().fromStyle(style);
			sectionList.add(section);
			return Optional.empty();
		}, text.getStyle());
		return this;
	}

	public ChatMessage join(ChatMessage message) {
		sectionList.addAll(message.getSectionList());
		return this;
	}

	public boolean isCompiled() {
		return compiledText != null;
	}

	public void purgeCache() {
		compiledText = null;
	}

	public synchronized Text build() {
		if (compiledText == null) {
			compiledText = Text.literal("");
			for (ChatSection section : sectionList) {
				compiledText.append(Text.literal(section.getText())
						.setStyle(section.getStyle().getStyle()));
			}
		}
		return compiledText;
	}
	
	public ChatMessage reset() {
		sectionList.clear();
		return this;
	}

	/**
	 * Prints this message client-side only, must be called in the render thread
	 */
	public void print() {
		ChatHud.getChatMessageQueue().add(() ->
			ChatHud.addMessage(this)
		);
	}

	@Deprecated
	public void sendMessage() {
		sendMessage(true);
	}

	/**
	 * Sends this message to the server, without any formatting
	 */
	@Deprecated
	public void sendMessage(boolean packet) {
		MainEntityPlayer player = Minecraft.getMinecraftGame()._getPlayer();
		if (player != null) {
			player.sendChatMessage(toString(false));
		}
	}

}
