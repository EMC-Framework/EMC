package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.util.types.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.EntryListWidget;

/**
 * @author Deftware
 */
public class StringList extends EntryListWidget<StringList.StringEntry> implements GenericComponent {

	public StringList(int width, int height, int top, int bottom, int itemHeight) {
		super(MinecraftClient.getInstance(), width, height, top, bottom, itemHeight);
	}

	public void clear() {
		this.children().clear();
	}

	public void addItem(ChatMessage text) {
		this.children().add(new StringEntry(text));
	}

	@Override
	public int getRowWidth() {
		return width - 2;
	}

	@Override
	protected int getScrollbarPosition() {
		return width - 6;
	}

	public static class StringEntry extends EntryListWidget.Entry<StringList.StringEntry> {

		private final ChatMessage string;
		private final Pair<Integer, ChatMessage> compiledText = new Pair<>(0, null);

		public StringEntry(ChatMessage string) {
			this.string = string;
		}

		public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
			if (compiledText.getRight() == null || compiledText.getLeft() != width) {
				compiledText.setLeft(width);
				compiledText.setRight(string.trimToWidth(width));
			}
			FontRenderer.drawString(compiledText.getRight(), x, y, 0xFFFFFF);
		}

	}

}