package me.deftware.client.framework.gui.title;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

/**
 * A wrapper for sending titles using the minecraft title system
 * (the big middle of screen things added in 1.8)
 *
 * @author Wagyourtail
 */
public class TitleAPI {

    public static void sendTitle(ChatMessage title, ChatMessage subtitle, int ticksFadeIn, int ticksVisible, int ticksFadeOut) {
        GuiIngame igh = Minecraft.getMinecraft().ingameGUI;
        if (igh != null) {
            //have to be done seperate because, minecraft...
            igh.displayTitle(title.toString(true), null, ticksFadeIn, ticksVisible, ticksFadeOut);
            igh.displayTitle(null, subtitle.toString(true), ticksFadeIn, ticksVisible, ticksFadeOut);
        }
    }

}
