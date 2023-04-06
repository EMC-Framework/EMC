package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.gui.Element;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;

import java.util.List;

/**
 * @author Deftware
 */
public class SelectableList<T extends SelectableList.ListItem> extends GuiSlot implements Element, Tooltipable, GenericComponent {

	private final List<T> delegate;

	private boolean extended = false;

	public SelectableList(List<T> delegate, int width, int height, int top, int bottom, int itemHeight) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, itemHeight);
		this.delegate = delegate;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	public interface ListItem {

		void render(int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta);

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
		this.selectedElement = -1;
	}

	public void setSelectedItem(T item) {
		this.selectedElement = this.delegate.indexOf(item);
		this.onSelectionUpdate(item);
	}

	public T getSelectedItem() {
		if (this.selectedElement != -1) {
			return this.delegate.get(selectedElement);
		}
		return null;
	}

	public T getHoveredItem(int mouseX, int mouseY) {
		int entry = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
		if (entry != -1) {
			return delegate.get(entry);
		}
		return null;
	}

	public void setScrollbarPosition(int y) {
		this.amountScrolled = y;
	}

	@Override
	public boolean isMouseOverComponent(int mouseX, int mouseY) {
		return this.getHoveredItem(mouseX, mouseY) != null;
	}

	@Override
	public List<String> getTooltipComponents(int mouseX, int mouseY) {
		int entry = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
		if (entry != -1) {
			Message[] tooltip = delegate.get(entry).getTooltip();
			if (tooltip != null && tooltip.length > 0) {
				return MinecraftScreen.getTooltipList(tooltip);
			}
		}
		return null;
	}

	@Override
	public int getListWidth() {
		if (extended) {
			return width - 12;
		}
		return super.getListWidth();
	}

	@Override
	protected int getScrollBarX() {
		if (extended) {
			return width - 6;
		}
		return super.getScrollBarX();
	}

	protected void onSelectionUpdate(T item) { }

	protected void onDrawItem(T item, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) { }

	@Override
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn) {
		T item = this.delegate.get(slotIndex);
		item.render(slotIndex, xPos, yPos, getListWidth(), heightIn, mouseXIn, mouseYIn, 0);
		this.onDrawItem(item, slotIndex, xPos, yPos, getListWidth(), heightIn, mouseXIn, mouseYIn, 0);
	}

	@Override
	protected int getSize() {
		return delegate.size();
	}

	@Override
	protected void elementClicked(int index, boolean b, int i1, int i2) {
		selectedElement = index;
		if (index != -1) {
			this.onSelectionUpdate(this.delegate.get(index));
		}
	}

	@Override
	protected boolean isSelected(int slotIndex) {
		return selectedElement == slotIndex;
	}

	@Override
	protected void drawBackground() { }

	private int previousIndex = -1;

	@Override
	public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
		if (previousIndex != getSize()) {
			selectedElement = -1;
			previousIndex = getSize();
		}
		super.drawScreen(mouseXIn, mouseYIn, partialTicks);
	}

}