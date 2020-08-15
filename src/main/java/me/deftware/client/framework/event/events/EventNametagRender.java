package me.deftware.client.framework.event.events;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Triggered when entity (including player) nametag is being rendered
 */
public class EventNametagRender extends Event {
    private final boolean isPlayer;
    private ChatMessage name;

    public EventNametagRender(Entity entity) {
        if (isPlayer = entity instanceof EntityPlayer) {
            name = new ChatMessage().fromString(entity.getName());
        }
    }

    public boolean isPlayer() {
        return this.isPlayer;
    }

    public ChatMessage getName() {
        return this.name;
    }
}
