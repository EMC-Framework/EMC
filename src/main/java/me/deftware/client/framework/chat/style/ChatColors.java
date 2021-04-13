package me.deftware.client.framework.chat.style;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.awt.*;

/**
 * @author Deftware
 */
public enum ChatColors {

	BLACK("BLACK", '0', 0, 0),
	DARK_BLUE("DARK_BLUE", '1', 1, 170),
	DARK_GREEN("DARK_GREEN", '2', 2, 43520),
	DARK_AQUA("DARK_AQUA", '3', 3, 43690),
	DARK_RED("DARK_RED", '4', 4, 11141120),
	DARK_PURPLE("DARK_PURPLE", '5', 5, 11141290),
	GOLD("GOLD", '6', 6, 16755200),
	GRAY("GRAY", '7', 7, 11184810),
	DARK_GRAY("DARK_GRAY", '8', 8, 5592405),
	BLUE("BLUE", '9', 9, 5592575),
	GREEN("GREEN", 'a', 10, 5635925),
	AQUA("AQUA", 'b', 11, 5636095),
	RED("RED", 'c', 12, 16733525),
	LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, 16733695),
	YELLOW("YELLOW", 'e', 14, 16777045),
	WHITE("WHITE", 'f', 15, 16777215);

	private final @Getter String name;
	private final @Getter char code;
	private final @Getter int index, rgb;
	private final @Getter ChatColor chatColor;

	ChatColors(String name, char code, int index, int rgb) {
		this.name = name;
		this.code = code;
		this.index = index;
		this.rgb = rgb;
		this.chatColor = new MinecraftColor(Formatting.byCode(code));
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public String toString() {
		return Formatting.byCode(code).toString();
	}

	/**
	 * Warning: If you create your own class implementing this, it will
	 * not be applied to Minecraft text (I.e. when a ChatMessage is compiled).
	 * However, it is supported by {@link me.deftware.client.framework.render.batching.font.FontRenderStack}
	 */
	public interface ChatColor {

		Color getColor();

		ChatColor deepCopy();

	}

	public static class MinecraftColor implements ChatColor {

		/**
		 * Old style legacy color system
		 */
		private @Setter Formatting formatting;

		private @Getter Color color;

		public MinecraftColor(Formatting formatting) {
			this.formatting = formatting;
			if (formatting != null && formatting.getColorValue() != null) {
				this.color = new Color(formatting.getColorValue());
			}
		}

		public Style applyToStyle(Style style) {
			if (formatting != null) {
				style = style.setColor(formatting);
			}
			return style;
		}

		@Override
		public String toString() {
			return formatting != null ? formatting.toString() : "";
		}

		public ChatColor deepCopy() {
			return new MinecraftColor(formatting);
		}

	}

}
