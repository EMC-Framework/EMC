package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.gui.GuiScreen;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;

/**
 * Triggered every time a game gui is displayed
 */
public class EventGuiScreenDisplay extends Event {

    private net.minecraft.client.gui.GuiScreen screen;
    private final ScreenTypes type;

    public EventGuiScreenDisplay(net.minecraft.client.gui.GuiScreen screen) {
        this.screen = screen;
        if (screen instanceof GuiMainMenu) {
            type = ScreenTypes.MainMenu;
        } else if (screen instanceof GuiMultiplayer) {
            type = ScreenTypes.Multiplayer;
        } else if (screen instanceof GuiIngameMenu) {
            type = ScreenTypes.GuiIngameMenu;
        } else {
            type = ScreenTypes.Unknown;
        }
    }

    public net.minecraft.client.gui.GuiScreen getScreen() {
        return screen;
    }

    public void setScreen(GuiScreen screen) {
        this.screen = screen;
    }

    public ScreenTypes getType() {
        return type;
    }

    public enum ScreenTypes {
        MainMenu, Multiplayer, GuiIngameMenu, Unknown
    }

}
