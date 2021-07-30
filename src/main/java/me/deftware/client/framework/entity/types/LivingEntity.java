package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.item.ItemStack;

/**
 * @author Deftware
 */
public class LivingEntity extends Entity {

	public LivingEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	public net.minecraft.entity.EntityLivingBase getLivingEntity() {
		return (net.minecraft.entity.EntityLivingBase) entity;
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

	private final ItemStack main = ItemStack.getEmpty();

	@Override
	public ItemStack getEntityHeldItem(boolean offhand) {
		return main.setStack(getLivingEntity().getHeldItem());
	}
	
}
