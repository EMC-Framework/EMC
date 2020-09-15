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
		super(Minecraft.getMinecraft(), width, height, topIn, bottomIn, slotHeightIn);
	}

	@Override
	protected int getSize() {
		return getISize();
	}

	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
		selectedSlot = slotIndex;
	}

	@Override
	protected boolean isSelected(int slotIndex) {
		return selectedSlot == slotIndex;
	}

	@Override
	protected void drawBackground() {

	}

	@Override
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn) {
		drawISlot(slotIndex, xPos, yPos);
	}

	protected abstract int getISize();

	protected abstract void drawISlot(int id, int x, int y);

	public int getSelectedSlot() {
		return selectedSlot;
	}

	public void doDraw(int mouseX, int mouseY, float partialTicks) {
		drawScreen(mouseX, mouseY, partialTicks);
	}

	public void clickElement(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
		elementClicked(slotIndex, isDoubleClick, mouseX, mouseY);
	}

}
