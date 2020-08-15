package me.deftware.client.framework.wrappers.gui;

import me.deftware.client.framework.utils.ResourceUtils;
import me.deftware.client.framework.wrappers.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.Tuple;

import java.util.Objects;

public class IScreens {

    public static GuiScreen translate(ScreenType type, IGuiScreen parent) {
        GuiScreen screen = null;
        if (type.equals(ScreenType.Multiplayer)) {
            screen = new GuiMultiplayer(parent);
        } else if (type.equals(ScreenType.MainMenu)) {
            screen = new GuiMainMenu();
        } else if (type.equals(ScreenType.WorldSelection)) {
            screen = new GuiWorldSelection(parent);
        } else if (type.equals(ScreenType.Options)) {
            screen = new GuiOptions(parent, Minecraft.getMinecraft().gameSettings);
        } else if (type.equals(ScreenType.Mods)) {
            // Non functional - TODO
            screen = IMinecraft.createScreenInstance("net.minecraftforge.fml.client.GuiModList", new Tuple<>(GuiScreen.class, parent));
        }
        return screen;
    }

    public static void displayGuiScreen(ScreenType type, IGuiScreen parent) {
        Minecraft.getMinecraft().displayGuiScreen(IScreens.translate(type, parent));
    }

    public static void switchToRealms(IGuiScreen parent) {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(parent);
    }

    public enum ScreenType {
        Multiplayer, WorldSelection, Options, MainMenu, Mods
    }

}
