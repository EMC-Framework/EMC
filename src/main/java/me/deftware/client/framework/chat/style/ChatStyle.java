package me.deftware.client.framework.chat.style;

import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.chat.event.ChatClickEvent;
import me.deftware.client.framework.chat.event.ChatHoverEvent;
import net.minecraft.ChatFormat;
import net.minecraft.network.chat.Style;

import java.util.regex.Pattern;

/**
 * @author Deftware
 */
public class ChatStyle {

	private static @Getter final char formattingChar = 167;

	private @Getter @Setter ChatColors.ChatColor color;
	private @Getter @Setter boolean bold, underline, italic, obfuscated, strikethrough;
	private @Getter @Setter ChatClickEvent clickEvent;
	private @Getter @Setter ChatHoverEvent hoverEvent;

	public void fromCode(String code) {
		if (code.equalsIgnoreCase("r")) { // Reset
			color = null;
			bold = underline = italic = obfuscated = strikethrough = false;
			return;
		}
		for (ChatFormat formatting : ChatFormat.values()) {
			if (formatting.toString().substring(1).equalsIgnoreCase(code)) {
				// Formatting
				if (formatting == ChatFormat.BOLD) bold = true;
				else if (formatting == ChatFormat.UNDERLINE) underline = true;
				else if (formatting == ChatFormat.ITALIC) italic = true;
				else if (formatting == ChatFormat.OBFUSCATED) obfuscated = true;
				else if (formatting == ChatFormat.STRIKETHROUGH) strikethrough = true;
				// Color
				else color = new ChatColors.ChatColor(formatting);
				break;
			}
		}
	}

	public void fromStyle(Style style) {
		// Color
		color = new ChatColors.ChatColor(style.getColor());
		// Formatting
		bold = style.isBold();
		underline = style.isUnderlined();
		italic = style.isItalic();
		obfuscated = style.isObfuscated();
		strikethrough = style.isStrikethrough();
		// Other
		if (style.getClickEvent() != null) clickEvent = ChatClickEvent.fromEvent(style.getClickEvent());
		if (style.getHoverEvent() != null) hoverEvent = ChatHoverEvent.fromEvent(style.getHoverEvent());
	}

	public Style getStyle() {
		Style style = new Style();
		// Formatting
		style = style
				.setBold(bold)
				.setItalic(italic)
				.setUnderline(underline)
				.setObfuscated(obfuscated)
				.setStrikethrough(strikethrough);
		// Color
		if (color != null) style = color.applyToStyle(style);
		// Other
		if (clickEvent != null) style = style.setClickEvent(clickEvent.toEvent());
		if (hoverEvent != null) style = style.setHoverEvent(hoverEvent.toEvent());
		return style;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// Color
		if (color != null) builder.append(color.toString());
		// Formatting
		if (bold) builder.append(ChatFormat.BOLD.toString());
		if (underline) builder.append(ChatFormat.UNDERLINE.toString());
		if (italic) builder.append(ChatFormat.ITALIC.toString());
		if (obfuscated) builder.append(ChatFormat.OBFUSCATED.toString());
		if (strikethrough) builder.append(ChatFormat.STRIKETHROUGH.toString());
		return builder.toString();
	}

	public ChatStyle deepCopy() {
		ChatStyle copy = new ChatStyle();
		if (color != null) copy.color = color.deepCopy();
		copy.bold = bold;
		copy.italic = italic;
		copy.obfuscated = obfuscated;
		copy.strikethrough = strikethrough;
		copy.clickEvent = clickEvent;
		copy.hoverEvent = hoverEvent;
		return copy;
	}

	public static String stripColors(String input) {
		return stripColors(input, ChatStyle.getFormattingChar());
	}

	public static String stripColors(String input, char formattingChar) {
		return Pattern.compile("(?i)" + formattingChar + "[0-9A-FK-OR]").matcher(input).replaceAll("");
	}

}
