package me.deftware.client.framework.event.events;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.event.Event;
import net.minecraft.client.sound.SoundInstance;

public class EventSound extends Event {
    private SoundInstance instance;
    private Message translationVal;

    public EventSound(SoundInstance instance, Message translationVal) {
        this.instance = instance;
    }

    public String getSoundId() {
        return instance.getId().toString();
    }

    public Message getSoundName() {
        return translationVal;
    }

    public double getX() {
        return instance.getX();
    }

    public double getY() {
        return instance.getY();
    }

    public double getZ() {
        return instance.getZ();
    }
}
