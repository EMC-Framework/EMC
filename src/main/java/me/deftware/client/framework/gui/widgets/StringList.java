package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.fonts.minecraft.FontRenderer;
import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.util.types.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Deftware
 */
public class StringList extends GuiListExtended {

	private final List<StringEntry> entryList = new ArrayList<>();

	public StringList(int width, int height, int top, int bottom, int itemHeight) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, itemHeight);
	}

	public void clear() {
		entryList.clear();
	}

	public void addItem(ChatMessage text) {
		entryList.add(new StringEntry(text));
	}

	public void doDraw(int mouseX, int mouseY, float delta) {
		this.drawScreen(mouseX, mouseY, delta);
	}

	public void addToEventListener(GuiScreen screen) { }

	@Override
	protected int getSize() {
		return entryList.size();
	}

	@Override
	public int getListWidth() {
		return width - 2;
	}

	@Override
	protected int getScrollBarX() {
		return width - 6;
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return entryList.get(index);
	}

	public static class StringEntry implements IGuiListEntry {

		private final ChatMessage string;
		private final Pair<Integer, ChatMessage> compiledText = new Pair<>(0, null);

		public StringEntry(ChatMessage string) {
			this.string = string;
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
			if (compiledText.getRight() == null || compiledText.getLeft() != entryWidth) {
				compiledText.setLeft(entryWidth);
				compiledText.setRight(string.trimToWidth(entryWidth));
			}
			FontRenderer.drawString(compiledText.getRight(), x, y, 0xFFFFFF);
		}

		@Override
		public void updatePosition(int slotIndex, int x, int y, float partialTicks) { }

		@Override
		public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
			return false;
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) { }

	}

}
