package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.mixin.imp.IMixinWorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.util.Objects;

/**
 * Triggered when entity (including player) nametag is being rendered
 */
public class EventNametagRender extends Event {

    private me.deftware.client.framework.entity.Entity entity;

    public EventNametagRender(Entity entity) {
        me.deftware.client.framework.entity.Entity emcEntity
                = Objects.requireNonNull(((IMixinWorldClient) net.minecraft.client.Minecraft.getInstance().world)).getLoadedEntitiesAccessor().getOrDefault(entity.getEntityId(), null);
        if (emcEntity != null) {
            this.entity = emcEntity;
        }
    }

    public me.deftware.client.framework.entity.Entity getEntity() {
        return entity;
    }

}
