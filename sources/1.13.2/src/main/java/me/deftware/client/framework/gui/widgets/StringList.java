package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.fonts.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.gl.GLX;

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

	public void addItem(Message text) {
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

		private final Message string;

		public StringEntry(Message string) {
			this.string = string;
		}

		@Override
		public void drawEntry(int width, int height, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
			FontRenderer.drawString(GLX.getInstance(), string, getX(), getY(), 0xFFFFFF);
		}

	}

}
