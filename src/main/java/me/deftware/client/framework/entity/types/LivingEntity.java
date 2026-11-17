package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.mixin.imp.IMixinEntity;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public class LivingEntity extends Entity {

	public LivingEntity(net.minecraft.world.entity.Entity entity) {
		super(entity);
	}

	@Override
	public net.minecraft.world.entity.LivingEntity getMinecraftEntity() {
		return (net.minecraft.world.entity.LivingEntity) entity;
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
		((IMixinEntityLivingBase) entity).setAirStrafingSpeed((float) multiplier);
	}

	public float getMovementMultiplier() {
		return ((IMixinEntityLivingBase) entity).getAirStrafingSpeed();
	}

	public int getHurtTime() {
		return getMinecraftEntity().hurtTime;
	}

	public boolean isClimbing() {
		return getMinecraftEntity().onClimbable();
	}

	public float getMoveStrafing() {
		return getMinecraftEntity().xxa;
	}

	public float getMoveForward() {
		return getMinecraftEntity().yya;
	}

	public void setAlive(boolean flag) {
		((IMixinEntity) getMinecraftEntity()).removeRemovedReason(); // TODO: Verify this
		getMinecraftEntity().setHealth(20f);
		getMinecraftEntity().absSnapTo(getPosX(), getPosY(), getPosZ());
	}

	public ItemStack getEntityHeldItem(EntityHand hand) {
		if (hand == EntityHand.OffHand)
			return (ItemStack) getMinecraftEntity().getItemInHand(InteractionHand.OFF_HAND);
		return (ItemStack) getMinecraftEntity().getItemInHand(InteractionHand.MAIN_HAND);
	}

	@Deprecated
	@Override
	public ItemStack getEntityHeldItem(boolean offhand) {
		return getEntityHeldItem(offhand ? EntityHand.OffHand : EntityHand.MainHand);
	}

	public AppliedEffect getStatusEffect(Effect effect) {
		return (AppliedEffect) getMinecraftEntity().getEffect(effect.getStatusEffect());
	}

	public void removeStatusEffect(Effect effect) {
		getMinecraftEntity().removeEffect(effect.getStatusEffect());
	}

	public boolean hasStatusEffect(Effect effect) {
		return getMinecraftEntity().hasEffect(effect.getStatusEffect());
	}

	public void addStatusEffect(AppliedEffect effect) {
		getMinecraftEntity().addEffect((MobEffectInstance) effect);
	}

	public Stream<AppliedEffect> getStatusEffects() {
		return getMinecraftEntity()
				.getActiveEffects()
				.stream()
				.map(AppliedEffect.class::cast);
	}

	public int getActiveStatusEffects() {
		return getMinecraftEntity().getActiveEffects().size();
	}

}
