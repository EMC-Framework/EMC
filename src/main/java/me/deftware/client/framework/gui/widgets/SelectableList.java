package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.gui.screens.MinecraftScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;

import java.util.List;

/**
 * @author Deftware
 */
public class SelectableList<T extends SelectableList.ListItem> extends GuiSlot implements Tooltipable, GenericComponent {

	private final List<T> delegate;

	private boolean extended = false;

	public SelectableList(List<T> delegate, int width, int height, int top, int bottom, int itemHeight) {
		super(Minecraft.getInstance(), width, height, top, bottom, itemHeight);
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

		default ChatMessage[] getTooltip() {
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
		int entry = this.getEntryAt(mouseX, mouseY);
		if (entry != -1) {
			return delegate.get(entry);
		}
		return null;
	}

	public void setScrollbarPosition(int y) {
		this.amountScrolled = y;
	}

	@Override
	protected boolean mouseClicked(int index, int p_195078_2_, double p_195078_3_, double p_195078_5_) {
		selectedElement = index;
		if (index != -1) {
			this.onSelectionUpdate(this.delegate.get(index));
		}
		return true;
	}

	@Override
	public boolean isMouseOverComponent(int mouseX, int mouseY) {
		return this.getHoveredItem(mouseX, mouseY) != null;
	}

	@Override
	public List<String> getTooltipComponents(int mouseX, int mouseY) {
		int entry = this.getEntryAt(mouseX, mouseY);
		if (entry != -1) {
			ChatMessage[] tooltip = delegate.get(entry).getTooltip();
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
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		T item = this.delegate.get(slotIndex);
		item.render(slotIndex, xPos, yPos, getListWidth(), heightIn, mouseXIn, mouseYIn, partialTicks);
		this.onDrawItem(item, slotIndex, xPos, yPos, getListWidth(), heightIn, mouseXIn, mouseYIn, partialTicks);
	}

	@Override
	protected int getSize() {
		return delegate.size();
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