package me.deftware.client.framework.chat.style;

import me.deftware.client.framework.chat.event.ChatClickEvent;
import me.deftware.client.framework.chat.event.ChatHoverEvent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import java.util.regex.Pattern;

/**
 * @author Deftware
 */
public class ChatStyle {
	private static final char formattingChar = 167;
	private ChatColors.ChatColor color;
	private boolean bold;
	private boolean underline;
	private boolean italic;
	private boolean obfuscated;
	private boolean strikethrough;
	private ChatClickEvent clickEvent;
	private ChatHoverEvent hoverEvent;

	public void fromCode(String code) {
		if (code.equalsIgnoreCase("r")) {
			// Reset
			color = null;
			bold = underline = italic = obfuscated = strikethrough = false;
			return;
		}
		for (TextFormatting formatting : TextFormatting.values()) {
			if (formatting.toString().substring(1).equalsIgnoreCase(code)) {
				// Formatting
				if (formatting == TextFormatting.BOLD) bold = true;
				 else if (formatting == TextFormatting.UNDERLINE) underline = true;
				 else if (formatting == TextFormatting.ITALIC) italic = true;
				 else if (formatting == TextFormatting.OBFUSCATED) obfuscated = true;
				 else if (formatting == TextFormatting.STRIKETHROUGH) strikethrough = true;
				 else 
				// Color
				color = new ChatColors.ChatColor(formatting);
				break;
			}
		}
	}

	public void fromStyle(Style style) {
		// Color
		if (style.getColor() != null) color = new ChatColors.ChatColor(style.getColor());
		// Formatting
		bold = style.getBold();
		underline = style.getUnderlined();
		italic = style.getItalic();
		obfuscated = style.getObfuscated();
		strikethrough = style.getStrikethrough();
		// Other
		if (style.getClickEvent() != null) clickEvent = ChatClickEvent.fromEvent(style.getClickEvent());
		if (style.getHoverEvent() != null) hoverEvent = ChatHoverEvent.fromEvent(style.getHoverEvent());
	}

	public Style getStyle() {
		Style style = new Style();
		// Formatting
		style = style.setBold(bold).setItalic(italic).setUnderlined(underline).setObfuscated(obfuscated).setStrikethrough(strikethrough);
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
		if (bold) builder.append(TextFormatting.BOLD.toString());
		if (underline) builder.append(TextFormatting.UNDERLINE.toString());
		if (italic) builder.append(TextFormatting.ITALIC.toString());
		if (obfuscated) builder.append(TextFormatting.OBFUSCATED.toString());
		if (strikethrough) builder.append(TextFormatting.STRIKETHROUGH.toString());
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

	public static char getFormattingChar() {
		return ChatStyle.formattingChar;
	}

	public ChatColors.ChatColor getColor() {
		return this.color;
	}

	public void setColor(final ChatColors.ChatColor color) {
		this.color = color;
	}

	public boolean isBold() {
		return this.bold;
	}

	public boolean isUnderline() {
		return this.underline;
	}

	public boolean isItalic() {
		return this.italic;
	}

	public boolean isObfuscated() {
		return this.obfuscated;
	}

	public boolean isStrikethrough() {
		return this.strikethrough;
	}

	public void setBold(final boolean bold) {
		this.bold = bold;
	}

	public void setUnderline(final boolean underline) {
		this.underline = underline;
	}

	public void setItalic(final boolean italic) {
		this.italic = italic;
	}

	public void setObfuscated(final boolean obfuscated) {
		this.obfuscated = obfuscated;
	}

	public void setStrikethrough(final boolean strikethrough) {
		this.strikethrough = strikethrough;
	}

	public ChatClickEvent getClickEvent() {
		return this.clickEvent;
	}

	public void setClickEvent(final ChatClickEvent clickEvent) {
		this.clickEvent = clickEvent;
	}

	public ChatHoverEvent getHoverEvent() {
		return this.hoverEvent;
	}

	public void setHoverEvent(final ChatHoverEvent hoverEvent) {
		this.hoverEvent = hoverEvent;
	}
}
