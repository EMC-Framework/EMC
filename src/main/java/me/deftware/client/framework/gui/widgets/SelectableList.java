package me.deftware.client.framework.gui.widgets;

import lombok.Setter;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.FormattedCharSequence;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Deftware
 */
public class SelectableList<T extends SelectableList.ListItem> extends AbstractSelectionList<SelectableList<T>.ItemEntry> implements Tooltipable, GenericComponent {

	private final List<T> delegate;

	@Setter
	private boolean extended = false;

	public SelectableList(List<T> delegate, int width, int height, int top, int bottom, int itemHeight) {
		super(Minecraft.getInstance(), width, bottom - top, top, itemHeight);
		this.delegate = delegate;
	}

	public interface ListItem {

		void render(GLX context, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta);

		default boolean onMouseClicked(double mouseX, double mouseY, int button) {
			return true;
		}

		default Message[] getTooltip() {
			return null;
		}

	}

	public void reset() {
		this.setScrollbarPosition(0);
		this.deselect();
	}

	public List<T> getDelegate() {
		return delegate;
	}

	public void deselect() {
		this.setSelected(null);
	}

	public void setSelectedItem(T item) {
		this.setSelected(this.map.get(item));
		this.onSelectionUpdate(item);
	}

	public T getSelectedItem() {
		if (this.getSelected() != null) {
			return this.getSelected().item;
		}
		return null;
	}

	public T getHoveredItem(int mouseX, int mouseY) {
		ItemEntry entry = this.getEntryAtPosition(mouseX, mouseY);
		if (entry != null) {
			return entry.item;
		}
		return null;
	}

	public void setScrollbarPosition(int y) {
		this.setScrollAmount(y);
	}

	@Override
	public boolean isMouseOverComponent(int mouseX, int mouseY) {
		return this.getHoveredItem(mouseX, mouseY) != null;
	}

	private final List<FormattedCharSequence> tooltipComponents = new ArrayList<>();

	@Override
	public List<FormattedCharSequence> getTooltipComponents(int mouseX, int mouseY) {
		ItemEntry entry = this.getEntryAtPosition(mouseX, mouseY);
		if (entry != null) {
			Message[] tooltip = entry.item.getTooltip();
			if (tooltip != null && tooltip.length > 0) {
				SelectableList.this._setTooltip(tooltipComponents, tooltip);
				return tooltipComponents;
			}
		}
		return null;
	}

	@Override
	public int getRowWidth() {
		if (extended) {
			return width - 12;
		}
		return super.getRowWidth();
	}

	@Override
	public int scrollBarX() {
		if (extended) {
			return width - 6;
		}
		return super.scrollBarX();
	}

	protected void onSelectionUpdate(T item) { }

	protected void onDrawItem(GLX context, T item, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) { }

	/*
		1.14+ logic
	 */

	private final Map<T, ItemEntry> map = new HashMap<>();

	@Override
	public void extractListItems(GuiGraphicsExtractor matrixStack, int mouseX, int mouseY, float tickDelta) {
		if (this.delegate.size() != this.map.size()) {
			this.rebuild();
		}
		super.extractListItems(matrixStack, mouseX, mouseY, tickDelta);
	}

	private void rebuild() {
		this.deselect();
		this.map.clear();
		this.clearEntries();
		for (T item : this.delegate) {
			ItemEntry entry = new ItemEntry(item);
			this.map.put(item, entry);
			this.addEntry(entry);
		}
	}

	protected class ItemEntry extends AbstractSelectionList.Entry<ItemEntry> {

		private final T item;

		public ItemEntry(T item) {
			this.item = item;
		}

		@Override
		public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			int x = this.getX() + 8;
			int y = this.getY();
			int entryWidth = this.getWidth();
			int entryHeight = this.getHeight();
			int index = SelectableList.this.children().indexOf(this);
			GLX glx = GLX.of(context);
			this.item.render(glx, index, x, y, entryWidth, entryHeight, mouseX, mouseY, tickDelta);
			SelectableList.this.onDrawItem(glx, item, index, x, y, entryWidth, entryHeight, mouseX, mouseY, tickDelta);
		}

		@Override
		public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
			if (this.item.onMouseClicked(event.x(), event.y(), GLFW.sdlButtonToGlfwButton(event.button()))) {
				SelectableList.this.setSelected(this);
				SelectableList.this.onSelectionUpdate(item);
				return true;
			}
			return false;
		}

	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput builder) { }

}
