package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;

/**
 * @author Deftware
 */
public class LivingEntity extends Entity {

	public LivingEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	@Override
	public net.minecraft.entity.EntityLiving getMinecraftEntity() {
		return (net.minecraft.entity.EntityLiving) entity;
	}

	public float getHealth() {
		return getMinecraftEntity().getHealth();
	}

	public float getHealthPercentage() {
		return (getHealth() * 100) / getMaxHealth();
	}

	public float getMaxHealth() {
		return getMinecraftEntity().getMaxHealth();
	}

	public void setMovementMultiplier(float multiplier) {
		getMinecraftEntity().jumpMovementFactor = multiplier;
	}

	public float getMovementMultiplier() {
		return getMinecraftEntity().jumpMovementFactor;
	}

	public int getHurtTime() {
		return getMinecraftEntity().hurtTime;
	}

	public boolean isClimbing() {
		return getMinecraftEntity().isOnLadder();
	}

	public float getMoveStrafing() {
		return getMinecraftEntity().moveStrafing;
	}

	public float getMoveForward() {
		return getMinecraftEntity().moveForward;
	}

	public void setAlive(boolean flag) {
		getMinecraftEntity().removed = false;
		getMinecraftEntity().setHealth(20f);
		getMinecraftEntity().setPosition(getPosX(), getPosY(), getPosZ());
	}
	
}
