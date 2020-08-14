package me.deftware.client.framework.wrappers;


import net.minecraft.client.Minecraft;

public class IGameSettings {

    public static int getLimitFramerate() {
        return Minecraft.getInstance().gameSettings.limitFramerate;
    }

    public static void setLimitFramerate(int framerate) {
        Minecraft.getInstance().gameSettings.limitFramerate = framerate;
        //Minecraft.getInstance().mainWindow.setFramerateLimit(framerate);
    }

}
