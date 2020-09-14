package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;

/**
 * @author Deftware
 */
public class LivingEntity extends Entity {

	public LivingEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	public net.minecraft.entity.EntityLiving getLivingEntity() {
		return (net.minecraft.entity.EntityLiving) entity;
	}

	public float getHealth() {
		return getLivingEntity().getHealth();
	}

	public float getHealthPercentage() {
		return (getHealth() * 100) / getMaxHealth();
	}

	public float getMaxHealth() {
		return getLivingEntity().getMaxHealth();
	}

	public void setMovementMultiplier(float multiplier) {
		getLivingEntity().jumpMovementFactor = multiplier;
	}

	public float getMovementMultiplier() {
		return getLivingEntity().jumpMovementFactor;
	}

	public int getHurtTime() {
		return getLivingEntity().hurtTime;
	}

	public boolean isClimbing() {
		return getLivingEntity().isOnLadder();
	}

	public float getMoveStrafing() {
		return getLivingEntity().moveStrafing;
	}

	public float getMoveForward() {
		return getLivingEntity().moveForward;
	}

	public void setAlive(boolean flag) {
		getLivingEntity().isDead = false;
		getLivingEntity().setHealth(20f);
		getLivingEntity().setPosition(getPosX(), getPosY(), getPosZ());
	}
	
}
