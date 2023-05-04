package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.mixin.imp.IMixinEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Hand;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public class LivingEntity extends Entity {

	public LivingEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	@Override
	public net.minecraft.entity.LivingEntity getMinecraftEntity() {
		return (net.minecraft.entity.LivingEntity) entity;
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

	public void setMovementMultiplier(double multiplier) {
		getMinecraftEntity().airStrafingSpeed = (float) multiplier;
	}

	public float getMovementMultiplier() {
		return getMinecraftEntity().airStrafingSpeed;
	}

	public int getHurtTime() {
		return getMinecraftEntity().hurtTime;
	}

	public boolean isClimbing() {
		return getMinecraftEntity().isClimbing();
	}

	public float getMoveStrafing() {
		return getMinecraftEntity().sidewaysSpeed;
	}

	public float getMoveForward() {
		return getMinecraftEntity().upwardSpeed;
	}

	public void setAlive(boolean flag) {
		((IMixinEntity) getMinecraftEntity()).removeRemovedReason(); // TODO: Verify this
		getMinecraftEntity().setHealth(20f);
		getMinecraftEntity().updatePosition(getPosX(), getPosY(), getPosZ());
	}

	public ItemStack getEntityHeldItem(EntityHand hand) {
		if (hand == EntityHand.OffHand)
			return (ItemStack) getMinecraftEntity().getStackInHand(Hand.OFF_HAND);
		return (ItemStack) getMinecraftEntity().getStackInHand(Hand.MAIN_HAND);
	}

	@Deprecated
	@Override
	public ItemStack getEntityHeldItem(boolean offhand) {
		return getEntityHeldItem(offhand ? EntityHand.OffHand : EntityHand.MainHand);
	}

	public AppliedEffect getStatusEffect(Effect effect) {
		return (AppliedEffect) getMinecraftEntity().getStatusEffect((StatusEffect) effect);
	}

	public void removeStatusEffect(Effect effect) {
		getMinecraftEntity().removeStatusEffect((StatusEffect) effect);
	}

	public boolean hasStatusEffect(Effect effect) {
		return getMinecraftEntity().hasStatusEffect((StatusEffect) effect);
	}

	public void addStatusEffect(AppliedEffect effect) {
		getMinecraftEntity().addStatusEffect((StatusEffectInstance) effect);
	}

	public Stream<AppliedEffect> getStatusEffects() {
		return getMinecraftEntity()
				.getStatusEffects()
				.stream()
				.map(AppliedEffect.class::cast);
	}

	public int getActiveStatusEffects() {
		return getMinecraftEntity().getStatusEffects().size();
	}

}
