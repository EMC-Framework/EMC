package me.deftware.client.framework.util.minecraft;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.world.World;
import net.minecraft.util.math.RayTraceResult;

import java.util.stream.Collectors;

public class EntitySwingResult {

    private final net.minecraft.entity.Entity hitEntity;

    public EntitySwingResult(net.minecraft.entity.Entity entity) {
        this.hitEntity = entity;
    }

    public Entity getEntity() {
        // Find entity in the world
        for (Entity entity : World.getLoadedEntities().collect(Collectors.toList())) {
            if (entity.getMinecraftEntity() == this.hitEntity)
                return entity;
        }
        return null;
    }

}
