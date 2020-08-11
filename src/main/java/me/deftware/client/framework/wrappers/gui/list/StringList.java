package me.deftware.client.framework.wrappers.gui.list;

import me.deftware.client.framework.wrappers.gui.IGuiScreen;
import me.deftware.client.framework.wrappers.render.IFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom implementation for Minecraft < 1.13
 */
@SuppressWarnings("ALL")
public class StringList extends GuiListExtended {

	private List<StringEntry> entryList = new ArrayList<>();

	public StringList(int width, int height, int top, int bottom, int itemHeight) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, itemHeight);
	}

	public void clear() {
		entryList.clear();
	}

	public void addItem(String text) {
		entryList.add(new StringEntry(text));
	}

	public void doDraw(int mouseX, int mouseY, float delta) {
		this.drawScreen(mouseX, mouseY, delta);
	}

	public void addToEventListener(IGuiScreen screen) {
		//screen.addEventListener(this);
	}

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

		private String string;

		public StringEntry(String string) {
			this.string = string;
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_) {
			String render = string;
			if (IFontRenderer.getStringWidth(render) > entryWidth) {
				render = "";
				for (String s : string.split("")) {
					if (IFontRenderer.getStringWidth(render + s + "...") < entryWidth - 6) {
						render += s;
					} else {
						render += "...";
						break;
					}
				}
			}
			IFontRenderer.drawString(render, x, y, 0xFFFFFF);
		}

		@Override
		public void setSelected(int slotIndex, int x, int y) {
			//
		}

		@Override
		public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
			return false;
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			//
		}
	}

}
