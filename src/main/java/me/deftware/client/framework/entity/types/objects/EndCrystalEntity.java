package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.math.Vector3;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.Vec3;
import me.deftware.client.framework.entity.Entity;

public class EndCrystalEntity extends Entity {

	public EndCrystalEntity(net.minecraft.world.entity.Entity entity) {
		super(entity);
	}

	public float getEntityDamage(Entity entity) {
		return ServerExplosion.getSeenPercent(this.entity.position(), entity.getMinecraftEntity());
	}
	
	public static float getExplosionExposure(Vector3<Double> vec, Entity entity) {
		return ServerExplosion.getSeenPercent((Vec3) vec, entity.getMinecraftEntity());
	}

}
