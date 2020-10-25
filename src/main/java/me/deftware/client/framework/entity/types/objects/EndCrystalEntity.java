package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.entity.Entity;
import net.minecraft.client.Minecraft;

public class EndCrystalEntity extends Entity {

	public EndCrystalEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	public float getEntityDamage(Entity entity) {
		return Minecraft.getMinecraft().world.getBlockDensity(entity.getMinecraftEntity().getPositionVector(), entity.getMinecraftEntity().getEntityBoundingBox());
	}

}
