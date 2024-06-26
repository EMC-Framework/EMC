package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;

public class EndCrystalEntity extends Entity {

	public EndCrystalEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	public float getEntityDamage(Entity entity) {
		return Explosion.getExposure(this.entity.getPos(), entity.getMinecraftEntity());
	}
	
	public static float getExplosionExposure(Vector3<Double> vec, Entity entity) {
		return Explosion.getExposure((Vec3d) vec, entity.getMinecraftEntity());
	}

}
