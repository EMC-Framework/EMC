package me.deftware.client.framework.wrappers.gui.list;

import me.deftware.client.framework.wrappers.gui.IGuiScreen;
import me.deftware.client.framework.wrappers.render.IFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

@SuppressWarnings("ALL")
public class StringList extends GuiListExtended {

	public StringList(int width, int height, int top, int bottom, int itemHeight) {
		super(Minecraft.getInstance(), width, height, top, bottom, itemHeight);
	}

	public void clear() {
		this.getChildren().clear();
	}

	public void addItem(String text) {
		this.getChildren().add(new StringEntry(text));
	}

	public void doDraw(int mouseX, int mouseY, float delta) {
		this.drawScreen(mouseX, mouseY, delta);
	}

	public void addToEventListener(IGuiScreen screen) {
		screen.addRawEventListener(this);
	}

	@Override
	public int getListWidth() {
		return width - 2;
	}

	@Override
	protected int getScrollBarX() {
		return width - 6;
	}

	public static class StringEntry extends IGuiListEntry {

		private String string;

		public StringEntry(String string) {
			this.string = string;
		}

		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
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
			IFontRenderer.drawString(render, getX(), getY(), 0xFFFFFF);
		}

	}

}
