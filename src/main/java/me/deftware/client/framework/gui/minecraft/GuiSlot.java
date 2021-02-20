package me.deftware.client.framework.gui.minecraft;

import me.deftware.client.framework.gui.GuiEventListener;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * @author Deftware
 */
public abstract class GuiSlot extends net.minecraft.client.gui.GuiSlot implements GuiEventListener {

	private int selectedSlot;

	public GuiSlot(int width, int height, int topIn, int bottomIn, int slotHeightIn) {
		super(Minecraft.getInstance(), width, height, topIn, bottomIn, slotHeightIn);
	}

	@Override
	protected int getSize() {
		return getISize();
	}

	@Override
	protected boolean mouseClicked(int index, int p_195078_2_, double p_195078_3_, double p_195078_5_) {
		selectedSlot = index;
		return true;
	}

	@Override
	protected boolean isSelected(int slotIndex) {
		return selectedSlot == slotIndex;
	}

	@Override
	protected void drawBackground() {

	}

	@Override
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		drawISlot(slotIndex, xPos, yPos);
	}

	protected abstract int getISize();

	protected abstract void drawISlot(int id, int x, int y);

	@Override
	public int getListWidth() {
		return getCustomRowWidth();
	}

	@Override
	protected int getScrollBarX() {
		return getCustomScrollbarPositionX();
	}

	public void resetScrollPosition() {
		this.scrollBy(-getAmountScrolled());
	}

	protected int getCustomRowWidth() {
		return 220;
	}

	protected int getCustomScrollbarPositionX() {
		return this.width / 2 + 124;
	}


	public int getSelectedSlot() {
		return selectedSlot;
	}

	public void doDraw(int mouseX, int mouseY, float partialTicks) {
		drawScreen(mouseX, mouseY, partialTicks);
	}

	public void clickElement(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
		mouseClicked(slotIndex, GLFW.GLFW_MOUSE_BUTTON_LEFT, mouseX, mouseY);
	}

}
