package me.deftware.client.framework.gui.minecraft;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

/**
 * @author Deftware
 */
public class ScreenInstance {

	private final GuiScreen screen;
	private final CommonScreenTypes type;

	public static ScreenInstance newInstance(GuiScreen screen) {
		if (screen instanceof GuiContainer) {
			return new ContainerScreenInstance(screen);
		}
		return new ScreenInstance(screen);
	}

	protected ScreenInstance(GuiScreen screen) {
		this.screen = screen;
		if (screen instanceof GuiDisconnected) {
			type = CommonScreenTypes.GuiDisconnected;
		} else if (screen instanceof GuiIngameMenu) {
			type = CommonScreenTypes.GuiIngameMenu;
		} else if (screen instanceof GuiContainer) {
			type = CommonScreenTypes.GuiContainer;
		} else {
			type = CommonScreenTypes.Unknown;
		}
	}

	public GuiScreen getMinecraftScreen() {
		return screen;
	}

	public CommonScreenTypes getType() {
		return type;
	}

	public void doDrawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
		screen.drawTexturedModalRect(x, y, u, v, width, height);
	}

	public enum CommonScreenTypes {
		GuiDisconnected, GuiIngameMenu, GuiContainer, Unknown
	}

}
