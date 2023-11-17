package me.deftware.client.framework.gui;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface Element {

	default void onMouseReleased(int x, int y, int button) { }

	default void onMouseClicked(int mouseX, int mouseY, int mouseButton) { }

	default void onKeyTyped(char typedChar, int keyCode) { }

}
