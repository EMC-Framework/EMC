package me.deftware.client.framework.wrappers.gui;

public interface CustomIGuiEventListener {

    void doHandleMouse();

    void doKeyTyped(char typedChar, int keyCode);

    void doMouseClicked(int mouseX, int mouseY, int mouseButton);

    void focusChanged(boolean state);

}