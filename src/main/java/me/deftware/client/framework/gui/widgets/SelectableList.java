package me.deftware.client.framework.gui.widgets;

import lombok.Setter;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Deftware
 */
public class SelectableList<T extends SelectableList.ListItem> extends EntryListWidget<SelectableList<T>.ItemEntry> implements Tooltipable, GenericComponent {

	private final List<T> delegate;

	@Setter
	private boolean extended = false;

	public SelectableList(List<T> delegate, int width, int height, int top, int bottom, int itemHeight) {
		super(MinecraftClient.getInstance(), width, bottom - top, top, itemHeight);
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
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().item;
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

	private final List<OrderedText> tooltipComponents = new ArrayList<>();

	@Override
	public List<OrderedText> getTooltipComponents(int mouseX, int mouseY) {
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
	protected int getScrollbarX() {
		if (extended) {
			return width - 6;
		}
		return super.getScrollbarX();
	}

	protected void onSelectionUpdate(T item) { }

	protected void onDrawItem(GLX context, T item, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) { }

	/*
		1.14+ logic
	 */

	private final Map<T, ItemEntry> map = new HashMap<>();

	@Override
	public void renderList(DrawContext matrixStack, int mouseX, int mouseY, float tickDelta) {
		if (this.delegate.size() != this.map.size()) {
			this.rebuild();
		}
		super.renderList(matrixStack, mouseX, mouseY, tickDelta);
	}

	private void rebuild() {
		this.deselect();
		this.map.clear();
		children().clear();
		for (T item : this.delegate) {
			ItemEntry entry = new ItemEntry(item);
			this.map.put(item, entry);
			children().add(entry);
		}
	}

	protected class ItemEntry extends EntryListWidget.Entry<ItemEntry> {

		private final T item;

		public ItemEntry(T item) {
			this.item = item;
		}

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			x += 8;
			GLX glx = GLX.of(context);
			this.item.render(glx, index, x, y, entryWidth, entryHeight, mouseX, mouseY, tickDelta);
			SelectableList.this.onDrawItem(glx, item, index, x, y, entryWidth, entryHeight, mouseX, mouseY, tickDelta);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.item.onMouseClicked(mouseX, mouseY, button)) {
				SelectableList.this.setSelected(this);
				SelectableList.this.onSelectionUpdate(item);
				return true;
			}
			return false;
		}

	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) { }

}
