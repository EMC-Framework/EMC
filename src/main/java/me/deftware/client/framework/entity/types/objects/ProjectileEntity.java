package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;

public class ProjectileEntity extends Entity {

    private Vector3<Double> lastPos;

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
