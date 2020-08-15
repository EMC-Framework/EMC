package me.deftware.client.framework.wrappers;


import net.minecraft.client.Minecraft;

public class IGameSettings {

    public static int getLimitFramerate() {
        return Minecraft.getMinecraft().gameSettings.limitFramerate;
    }

    public static void setLimitFramerate(int framerate) {
        Minecraft.getMinecraft().gameSettings.limitFramerate = framerate;
        //Minecraft.getMinecraft().mainWindow.setFramerateLimit(framerate);
    }

}
