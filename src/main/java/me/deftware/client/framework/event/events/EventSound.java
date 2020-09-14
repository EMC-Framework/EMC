package me.deftware.client.framework.event.events;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.Event;
import net.minecraft.client.audio.ISound;

public class EventSound extends Event {

    private ISound instance;
    private ChatMessage translationVal;

    public EventSound(ISound instance, ChatMessage translationVal) {
        this.instance = instance;
    }

    public String getSoundId() {
        return instance.getSoundLocation().toString();
    }

    public ChatMessage getSoundName() {
        return translationVal;
    }

    public double getX() {
        return instance.getXPosF();
    }

    public double getY() {
        return instance.getYPosF();
    }

    public double getZ() {
        return instance.getZPosF();
    }

}
