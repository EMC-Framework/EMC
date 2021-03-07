package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.vector.Vector3d;
import net.minecraft.entity.projectile.EntityArrow;

public class ProjectileEntity extends Entity {

    private Vector3d lastPos;

    public ProjectileEntity(net.minecraft.entity.Entity entity) {
        super(entity);
    }

    @Override
    public EntityArrow getMinecraftEntity() {
        return (EntityArrow) this.entity;
    }

    public boolean isMoving() {
        if (lastPos != null && lastPos.equals(getPosition()))
            return false;
        lastPos = getPosition();
        return true;
    }

}
