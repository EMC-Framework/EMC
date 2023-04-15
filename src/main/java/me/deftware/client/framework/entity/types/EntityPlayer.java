package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.objects.ClonedPlayerMP;
import me.deftware.client.framework.inventory.EntityInventory;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.potion.PotionEffect;

import java.util.*;

/**
 * @author Deftware
 */
public class EntityPlayer extends LivingEntity {

	private final EntityInventory inventory;

	public EntityPlayer(net.minecraft.entity.player.EntityPlayer entity) {
		super(entity);
		inventory = new EntityInventory(entity);
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
		return getMinecraftEntity().capabilities.isFlying;
	}

	public void setFlying(boolean flag) {
		getMinecraftEntity().capabilities.isFlying = flag;
	}

	public EntityInventory getInventory() {
		return inventory;
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
		return net.minecraft.client.Minecraft.getMinecraft().world.getCollisionBoxes(getMinecraftEntity(), getMinecraftEntity().getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty();
	}

	public void openInventory() {
		Minecraft.getMinecraftGame().runOnRenderThread(() -> net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(getMinecraftEntity())));
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
		return getMinecraftEntity().capabilities.getFlySpeed();
	}

	public void setFlySpeed(float speed) {
		getMinecraftEntity().capabilities.setFlySpeed(speed);
	}

	public float getWalkSpeed() {
		return getMinecraftEntity().capabilities.getWalkSpeed();
	}

	public void setWalkSpeed(float speed) {
		getMinecraftEntity().capabilities.setPlayerWalkSpeed(speed);
	}

}
