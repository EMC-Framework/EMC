package me.deftware.client.framework.util.minecraft;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.world.ClientWorld;

public class EntitySwingResult {

    private final net.minecraft.entity.Entity hitEntity;

    public EntitySwingResult(net.minecraft.entity.Entity entity) {
        this.hitEntity = entity;
    }

    public Entity getEntity() {
        return ClientWorld.getClientWorld().getEntityByReference(this.hitEntity);
    }

}
