package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.message.Message;
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

	public void addItem(Message text) {
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

		private final Message string;

		public StringEntry(Message string) {
			this.string = string;
		}

		public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
			FontRenderer.drawString(string, x, y, 0xFFFFFF);
		}

	}

}