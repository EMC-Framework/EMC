package me.deftware.client.framework.gui;

/**
 * @author Deftware
 */
public interface GuiEventListener {

	void doHandleMouse();

	void doKeyTyped(char typedChar, int keyCode);

	void doMouseClicked(int mouseX, int mouseY, int mouseButton);

	void focusChanged(boolean state);

}
