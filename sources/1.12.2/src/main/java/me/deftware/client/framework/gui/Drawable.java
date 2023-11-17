package me.deftware.client.framework.gui;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface Drawable {

	void onRender(int mouseX, int mouseY, float delta);

}
