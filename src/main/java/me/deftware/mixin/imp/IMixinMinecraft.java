package me.deftware.mixin.imp;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface IMixinMinecraft {

    Session getSession();

    void setSession(Session session);

    Timer getTimer();

    void displayGuiScreen(GuiScreen guiScreenIn);

    void doRightClickMouse();

    void doClickMouse();

    void doMiddleClickMouse();

    void setRightClickDelayTimer(int delay);

    boolean getIsWindowFocused();

    int getFPS();

    void setSessionService(MinecraftSessionService service);

}
