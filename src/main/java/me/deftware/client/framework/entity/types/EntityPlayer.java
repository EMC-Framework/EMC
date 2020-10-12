package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.conversion.ConvertedList;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.objects.ClonedPlayerMP;
import me.deftware.client.framework.inventory.Inventory;
import me.deftware.client.framework.item.effect.AppliedStatusEffect;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.potion.PotionEffect;

import java.util.List;
import java.util.UUID;

/**
 * @author Deftware
 */
public class EntityPlayer extends LivingEntity {

	private final Inventory inventory;
	private final ConvertedList<AppliedStatusEffect, PotionEffect> statusEffects;

	public EntityPlayer(net.minecraft.entity.player.EntityPlayer entity) {
		super(entity);
		inventory = new Inventory(entity);
		this.statusEffects = new ConvertedList<>(() -> getMinecraftEntity().getActivePotionEffects(), null, AppliedStatusEffect::new);
	}

	public boolean isUsingItem() {
		return getMinecraftEntity().isHandActive();
	}

	public boolean isCreative() {
		return getMinecraftEntity().isCreative();
	}

	public boolean isSleeping() {
		return getMinecraftEntity().isPlayerSleeping();
	}

	public boolean isFlying() {
		return getMinecraftEntity().abilities.isFlying;
	}

	public void setFlying(boolean flag) {
		getMinecraftEntity().abilities.isFlying = flag;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public List<AppliedStatusEffect> getStatusEffects() {
		return statusEffects.poll();
	}

	public float getSaturationLevel() {
		return getMinecraftEntity().getFoodStats().getSaturationLevel();
	}

	public UUID getUUID() {
		return ((net.minecraft.entity.player.EntityPlayer) entity).getGameProfile().getId();
	}

	public float getRotationHeadYaw() {
		return getMinecraftEntity().rotationYawHead;
	}

	public String getUsername() {
		return ((net.minecraft.entity.player.EntityPlayer) entity).getGameProfile().getName();
	}

	@Override
	public net.minecraft.entity.player.EntityPlayer getMinecraftEntity() {
		return (net.minecraft.entity.player.EntityPlayer) entity;
	}

	public float getCooldown() {
		return getMinecraftEntity().getCooledAttackStrength(0);
	}

	public boolean isAtEdge() {
		return net.minecraft.client.Minecraft.getInstance().world.getCollisionBoxes(getMinecraftEntity(), getMinecraftEntity().getBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).count() == 0;
	}

	public void openInventory() {
		Minecraft.RENDER_THREAD.add(() -> net.minecraft.client.Minecraft.getInstance().displayGuiScreen(new GuiInventory(getMinecraftEntity())));
	}

	public int getFoodLevel() {
		return getMinecraftEntity().getFoodStats().getFoodLevel();
	}

	public void respawn() {
		getMinecraftEntity().respawnPlayer();
	}

	public int getItemInUseCount() {
		return ((IMixinEntityLivingBase) getMinecraftEntity()).getActiveItemStackUseCount(); // TODO
	}

	public void doJump() {
		getMinecraftEntity().jump();
	}

	public int getItemInUseMaxCount() {
		return getMinecraftEntity().getItemInUseMaxCount();
	}

	public void drawPlayer(int posX, int posY, int scale) {
		GuiInventory.drawEntityOnScreen(posX, posY, scale, 0, 0, getMinecraftEntity());
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
