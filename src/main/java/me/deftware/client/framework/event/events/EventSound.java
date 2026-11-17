package me.deftware.client.framework.event.events;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.resources.sounds.SoundInstance;
import me.deftware.client.framework.event.Event;

public class EventSound extends Event {
    private SoundInstance instance;
    private Message translationVal;

    public EventSound(SoundInstance instance, Message translationVal) {
        this.instance = instance;
    }

    public String getSoundId() {
        return instance.getIdentifier().toString();
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
