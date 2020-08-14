package me.deftware.client.framework.wrappers.gui.imp;

import net.minecraft.client.gui.GuiScreen;

public class ScreenInstance {

    public GuiScreen screen;

    public ScreenInstance(GuiScreen screen) {
        this.screen = screen;
    }

    public void doDrawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        screen.drawTexturedModalRect(x, y, u, v, width, height);
    }

}

