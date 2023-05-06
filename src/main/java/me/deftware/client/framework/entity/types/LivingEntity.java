package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.stream.Stream;

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

	public void setMovementMultiplier(double multiplier) {
		getLivingEntity().jumpMovementFactor = (float) multiplier;
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

	public ItemStack getEntityHeldItem(EntityHand hand) {
		if (getLivingEntity().getHeldItem() == null) {
			return ItemStack.EMPTY;
		}
		return (ItemStack) getLivingEntity().getHeldItem();
	}

	@Deprecated
	@Override
	public ItemStack getEntityHeldItem(boolean offhand) {
		return getEntityHeldItem(offhand ? EntityHand.OffHand : EntityHand.MainHand);
	}

	public AppliedEffect getStatusEffect(Effect effect) {
		return (AppliedEffect) getLivingEntity().getActivePotionEffect((Potion) effect);
	}

	public void removeStatusEffect(Effect effect) {
		getLivingEntity().removePotionEffect(((Potion) effect).getId());
	}

	public boolean hasStatusEffect(Effect effect) {
		return getLivingEntity().isPotionActive((Potion) effect);
	}

	public void addStatusEffect(AppliedEffect effect) {
		getLivingEntity().addPotionEffect((PotionEffect) effect);
	}

	public Stream<AppliedEffect> getStatusEffects() {
		return getLivingEntity()
				.getActivePotionEffects()
				.stream()
				.map(AppliedEffect.class::cast);
	}

	public int getActiveStatusEffects() {
		return getLivingEntity().getActivePotionEffects().size();
	}

}
