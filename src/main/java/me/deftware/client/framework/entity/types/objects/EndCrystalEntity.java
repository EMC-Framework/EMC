package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class EndCrystalEntity extends Entity {

	public EndCrystalEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	public float getEntityDamage(Entity entity) {
		return Minecraft.getInstance().world.getBlockDensity(entity.getMinecraftEntity().getPositionVector(), entity.getMinecraftEntity().getBoundingBox());
	}
	
	public static float getExplosionExposure(Vector3<Double> vec, Entity entity) {
		return Minecraft.getInstance().world.getBlockDensity((Vec3d) vec, entity.getMinecraftEntity().getBoundingBox());
	}

}
