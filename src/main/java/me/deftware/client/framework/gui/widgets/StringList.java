package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.util.types.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

/**
 * @author Deftware
 */
public class StringList extends GuiListExtended<StringList.StringEntry> implements GenericComponent {

	public StringList(int width, int height, int top, int bottom, int itemHeight) {
		super(Minecraft.getInstance(), width, height, top, bottom, itemHeight);
	}

	public void clear() {
		this.getChildren().clear();
	}

	public void addItem(ChatMessage text) {
		this.getChildren().add(new StringEntry(text));
	}

	@Override
	public int getListWidth() {
		return width - 2;
	}

	@Override
	protected int getScrollBarX() {
		return width - 6;
	}

	public static class StringEntry extends GuiListExtended.IGuiListEntry<StringList.StringEntry> {

		private final ChatMessage string;
		private final Pair<Integer, ChatMessage> compiledText = new Pair<>(0, null);

		public StringEntry(ChatMessage string) {
			this.string = string;
		}

		@Override
		public void drawEntry(int width, int height, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
			if (compiledText.getRight() == null || compiledText.getLeft() != width) {
				compiledText.setLeft(width);
				compiledText.setRight(string.trimToWidth(width));
			}
			FontRenderer.drawString(compiledText.getRight(), getX(), getY(), 0xFFFFFF);
		}

	}

}
