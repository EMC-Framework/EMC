package me.deftware.client.framework.entity.types;

import com.google.common.collect.Iterables;
import me.deftware.client.framework.conversion.ConvertedList;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Deftware
 */
public class LivingEntity extends Entity {

	public LivingEntity(net.minecraft.entity.Entity entity) {
		super(entity);
		this.heldItems = new ConvertedList<>(() -> Collections.singleton(getLivingEntity().getHeldItem()),pair ->
				pair.getLeft().getMinecraftItemStack() == Iterables.get(Collections.singleton(getLivingEntity().getHeldItem()), pair.getRight())
				, ItemStack::new);
		this.armourItems = new ConvertedList<>(() -> Arrays.asList(getLivingEntity().getInventory()), pair ->
				pair.getLeft().getMinecraftItemStack() == Iterables.get(Arrays.asList(getLivingEntity().getInventory()), pair.getRight())
				, ItemStack::new);
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
