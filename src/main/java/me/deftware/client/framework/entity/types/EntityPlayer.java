package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.objects.ClonedPlayerMP;
import me.deftware.client.framework.inventory.EntityInventory;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.shape.VoxelShape;

import java.util.*;

/**
 * @author Deftware
 */
public class EntityPlayer extends LivingEntity {

	public EntityPlayer(PlayerEntity entity) {
		super(entity);
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
		return getMinecraftEntity().getAbilities().flying;
	}

	public void setFlying(boolean flag) {
		getMinecraftEntity().getAbilities().flying = flag;
	}

	public EntityInventory getInventory() {
		return (EntityInventory) getMinecraftEntity().getInventory();
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
		Iterable<VoxelShape> iterable = Objects.requireNonNull(MinecraftClient.getInstance().world).getCollisions(getMinecraftEntity(), getMinecraftEntity().getBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001));
		return !iterable.iterator().hasNext();
	}

	public void openInventory() {
		Minecraft.getMinecraftGame().runOnRenderThread(() -> MinecraftClient.getInstance().setScreen(new InventoryScreen(getMinecraftEntity())));
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

	public void drawPlayer(GLX context, int posX, int posY, int scale) {
		InventoryScreen.drawEntity(posX, posY, scale, 0, 0, getMinecraftEntity());
	}

	public Entity clone() {
		return Entity.newInstance(new ClonedPlayerMP(getMinecraftEntity()));
	}

	public float getFlySpeed() {
		return getMinecraftEntity().getAbilities().getFlySpeed();
	}

	public void setFlySpeed(float speed) {
		getMinecraftEntity().getAbilities().setFlySpeed(speed);
	}

	public float getWalkSpeed() {
		return getMinecraftEntity().getAbilities().getWalkSpeed();
	}

	public void setWalkSpeed(float speed) {
		getMinecraftEntity().getAbilities().setWalkSpeed(speed);
	}

}
