package me.deftware.client.framework.gui.minecraft;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;

/**
 * @author Deftware
 */
public class ScreenInstance {

	private final Screen screen;
	private final CommonScreenTypes type;

	public static ScreenInstance newInstance(Screen screen) {
		if (screen instanceof ContainerScreen<?>) {
			return new ContainerScreenInstance(screen);
		}
		return new ScreenInstance(screen);
	}

	protected ScreenInstance(Screen screen) {
		this.screen = screen;
		if (screen instanceof DisconnectedScreen) {
			type = CommonScreenTypes.GuiDisconnected;
		} else if (screen instanceof GameMenuScreen) {
			type = CommonScreenTypes.GuiIngameMenu;
		} else if (screen instanceof ContainerScreen<?>) {
			type = CommonScreenTypes.GuiContainer;
		} else {
			type = CommonScreenTypes.Unknown;
		}
	}

	public Screen getMinecraftScreen() {
		return screen;
	}

	public CommonScreenTypes getType() {
		return type;
	}

	public void doDrawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
		screen.blit(x, y, u, v, width, height);
	}

	public enum CommonScreenTypes {
		GuiDisconnected, GuiIngameMenu, GuiContainer, Unknown
	}

}
