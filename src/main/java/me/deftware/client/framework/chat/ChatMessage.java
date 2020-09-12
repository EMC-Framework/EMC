package me.deftware.client.framework.chat;

import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.chat.style.ChatStyle;
import me.deftware.client.framework.fonts.minecraft.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deftware
 */
public class ChatMessage {
	private TextComponentString compiledText; /* Cache */
	protected final List<ChatSection> sectionList = new ArrayList<>();

	public String toString(boolean withFormatting) {
		StringBuilder builder = new StringBuilder();
		for (ChatSection section : sectionList) {
			builder.append(withFormatting ? TextFormatting.RESET.toString() + section.getStyle().toString() : "").append(section.getText());
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
				if (section.length() == 1) {
					// Only formatting, no text
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

	public ChatMessage fromText(ITextComponent text) {
		// This function is highly dependant on which Minecraft version this is implemented on
		for (ITextComponent component : text) {
			ChatSection section = new ChatSection(component.getString());
			section.getStyle().fromStyle(component.getStyle());
			sectionList.add(section);
		}
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

	public synchronized TextComponentString build() {
		if (compiledText == null) {
			compiledText = new TextComponentString("");
			for (ChatSection section : sectionList) {
				compiledText.appendSibling(new TextComponentString(section.getText()).setStyle(section.getStyle().getStyle()));
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
		ChatHud.getChatMessageQueue().add(() -> ChatHud.addMessage(this));
	}

	public void sendMessage() {
		sendMessage(true);
	}

	/**
	 * Sends this message to the server, without any formatting
	 */
	public void sendMessage(boolean packet) {
		if (net.minecraft.client.Minecraft.getInstance().player != null) {
			ChatHud.getChatMessageQueue().add(() -> {
				if (packet) {
					net.minecraft.client.Minecraft.getInstance().player.connection.sendPacket(new CPacketChatMessage(toString(false)));
				} else {
					net.minecraft.client.Minecraft.getInstance().player.sendChatMessage(toString(false));
				}
			});
		}
	}

	public List<ChatSection> getSectionList() {
		return this.sectionList;
	}
}
