package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;

public class ProjectileEntity extends Entity {

    private Vector3<Double> lastPos;

    public ProjectileEntity(net.minecraft.entity.Entity entity) {
        super(entity);
    }

    @Override
    public net.minecraft.entity.projectile.ProjectileEntity getMinecraftEntity() {
        return (net.minecraft.entity.projectile.ProjectileEntity) this.entity;
    }

    public boolean isMoving() {
        if (lastPos != null && lastPos.equals(getPosition()))
            return false;
        lastPos = getPosition();
        return true;
    }

}
