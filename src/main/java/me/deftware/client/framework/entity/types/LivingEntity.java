package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

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
		getLivingEntity().removed = false;
		getLivingEntity().setHealth(20f);
		getLivingEntity().setPosition(getPosX(), getPosY(), getPosZ());
	}

	private final ItemStack main = ItemStack.getEmpty(), offhand = ItemStack.getEmpty();

	@Override
	public ItemStack getEntityHeldItem(boolean offhand) {
		if (offhand)
			return this.offhand.setStack(getLivingEntity().getHeldItem(EnumHand.OFF_HAND));
		return main.setStack(getLivingEntity().getHeldItem(EnumHand.MAIN_HAND));
	}

	public AppliedEffect getStatusEffect(Effect effect) {
		return (AppliedEffect) getLivingEntity().getActivePotionEffect((Potion) effect);
	}

	public void removeStatusEffect(Effect effect) {
		getLivingEntity().removePotionEffect((Potion) effect);
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
