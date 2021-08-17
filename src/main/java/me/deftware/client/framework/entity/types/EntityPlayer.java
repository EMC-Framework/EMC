package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.objects.ClonedPlayerMP;
import me.deftware.client.framework.inventory.EntityInventory;
import me.deftware.client.framework.item.effect.AppliedStatusEffect;
import me.deftware.client.framework.item.effect.StatusEffect;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.*;

/**
 * @author Deftware
 */
public class EntityPlayer extends LivingEntity {

	private final EntityInventory inventory;
	private final Map<net.minecraft.entity.effect.StatusEffect, AppliedStatusEffect> statusEffects = new HashMap<>();

	public EntityPlayer(PlayerEntity entity) {
		super(entity);
		inventory = new EntityInventory(entity);
	}

	public boolean isUsingItem() {
		return getMinecraftEntity().isUsingItem();
	}

	public boolean isCreative() {
		return getMinecraftEntity().isCreative();
	}

	public boolean isSleeping() {
		return getMinecraftEntity().isSleeping();
	}

	public boolean isFlying() {
		return getMinecraftEntity().abilities.flying;
	}

	public void setFlying(boolean flag) {
		getMinecraftEntity().abilities.flying = flag;
	}

	public EntityInventory getInventory() {
		return inventory;
	}

	public AppliedStatusEffect getStatusEffect(StatusEffect effect) {
		return statusEffects.get(effect.getMinecraftStatusEffect());
	}

	public void removeStatusEffect(StatusEffect effect) {
		getMinecraftEntity().removeStatusEffect(effect.getMinecraftStatusEffect());
	}

	public boolean hasStatusEffect(StatusEffect effect) {
		return getMinecraftEntity().hasStatusEffect(effect.getMinecraftStatusEffect());
	}

	public void addStatusEffect(AppliedStatusEffect effect) {
		getMinecraftEntity().addStatusEffect(effect.getMinecraftStatusEffectInstance());
	}

	public Collection<AppliedStatusEffect> getStatusEffects() {
		PlayerEntity player = getMinecraftEntity();
		int size = player.getStatusEffects().size();
		if (size != 0) {
			if (statusEffects.keySet().size() != size) {
				for (StatusEffectInstance effect : player.getStatusEffects()) {
					if (!statusEffects.containsKey(effect.getEffectType()))
						statusEffects.put(effect.getEffectType(), new AppliedStatusEffect(effect));
					else
						statusEffects.get(effect.getEffectType()).setInstance(effect);
				}
				// Remove old effects
				if (statusEffects.keySet().size() != size)
					statusEffects.keySet().removeIf(e -> !player.hasStatusEffect(e));
			}
			return statusEffects.values();
		}
		return Collections.emptyList();
	}

	public float getSaturationLevel() {
		return getMinecraftEntity().getHungerManager().getSaturationLevel();
	}

	public UUID getUUID() {
		return ((PlayerEntity) entity).getGameProfile().getId();
	}

	public float getRotationHeadYaw() {
		return getMinecraftEntity().headYaw;
	}

	public String getUsername() {
		return ((PlayerEntity) entity).getGameProfile().getName();
	}

	@Override
	public PlayerEntity getMinecraftEntity() {
		return (PlayerEntity) entity;
	}

	public float getCooldown() {
		return getMinecraftEntity().getAttackCooldownProgress(0);
	}

	public boolean isAtEdge() {
		return Objects.requireNonNull(MinecraftClient.getInstance().world).getCollisions(getMinecraftEntity(), getMinecraftEntity().getBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001), Collections.emptySet()).count() == 0;
	}

	public void openInventory() {
		Minecraft.getMinecraftGame().runOnRenderThread(() -> MinecraftClient.getInstance().openScreen(new InventoryScreen(getMinecraftEntity())));
	}

	public int getFoodLevel() {
		return getMinecraftEntity().getHungerManager().getFoodLevel();
	}

	public void respawn() {
		getMinecraftEntity().requestRespawn();
	}

	public int getItemInUseCount() {
		return ((IMixinEntityLivingBase) getMinecraftEntity()).getActiveItemStackUseCount(); // TODO
	}

	public void doJump() {
		getMinecraftEntity().jump();
	}

	public int getItemInUseMaxCount() {
		return getMinecraftEntity().getItemUseTimeLeft();
	}

	public void drawPlayer(int posX, int posY, int scale) {
		InventoryScreen.drawEntity(posX, posY, scale, 0, 0, getMinecraftEntity());
	}

	public Entity clone() {
		return Entity.newInstance(new ClonedPlayerMP(getMinecraftEntity()));
	}

	public float getFlySpeed() {
		return getMinecraftEntity().abilities.getFlySpeed();
	}

	public void setFlySpeed(float speed) {
		getMinecraftEntity().abilities.setFlySpeed(speed);
	}

	public float getWalkSpeed() {
		return getMinecraftEntity().abilities.getWalkSpeed();
	}

	public void setWalkSpeed(float speed) {
		getMinecraftEntity().abilities.setWalkSpeed(speed);
	}

}
