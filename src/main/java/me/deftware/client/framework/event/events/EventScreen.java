package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author Deftware
 */
public class EventScreen extends Event {

	private final GuiScreen screen;

	private Type type = Type.Init;

	private int mouseX, mouseY;

	public EventScreen(GuiScreen screen) {
		this.screen = screen;
	}

	public EventScreen setType(Type type) {
		setCanceled(false);
		this.type = type;
		return this;
	}

	public MinecraftScreen getScreen() {
		return (MinecraftScreen) screen;
	}

	public Type getType() {
		return type;
	}

	public int getMouseX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}

	public enum Type {

		/**
		 * When the screen is constructed
		 */
		Init,

		/**
		 * When the screen is initially set up, or each time
		 * the screen is resized
		 */
		Setup,

		/**
		 * Called 20 times a second
		 */
		Tick,

		/**
		 * When the screen is drawn
		 */
		Draw, PostDraw

	}

}
