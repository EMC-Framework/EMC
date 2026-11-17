package me.deftware.client.framework.entity.types;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.objects.ClonedPlayerMP;
import me.deftware.client.framework.inventory.EntityInventory;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.*;

/**
 * @author Deftware
 */
public class EntityPlayer extends LivingEntity {

	public EntityPlayer(Player entity) {
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
		return getMinecraftEntity().getFoodData().getSaturationLevel();
	}

	public UUID getUUID() {
		return ((Player) entity).getGameProfile().id();
	}

	public float getRotationHeadYaw() {
		return getMinecraftEntity().yHeadRot;
	}

	public String getUsername() {
		return ((Player) entity).getGameProfile().name();
	}

	@Override
	public Player getMinecraftEntity() {
		return (Player) entity;
	}

	public float getCooldown() {
		return getMinecraftEntity().getAttackStrengthScale(0);
	}

	public boolean isAtEdge() {
		Iterable<VoxelShape> iterable = Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().level).getCollisions(getMinecraftEntity(), getMinecraftEntity().getBoundingBox().move(0, -0.5, 0).inflate(-0.001, 0, -0.001));
		return !iterable.iterator().hasNext();
	}

	public void openInventory() {
		Minecraft.getMinecraftGame().runOnRenderThread(() -> net.minecraft.client.Minecraft.getInstance().gui.setScreen(new InventoryScreen(getMinecraftEntity())));
	}

	public int getFoodLevel() {
		return getMinecraftEntity().getFoodData().getFoodLevel();
	}

	public void respawn() {
		if (entity instanceof net.minecraft.client.player.LocalPlayer localPlayer) {
			localPlayer.respawn();
		}
	}

	public int getItemInUseCount() {
		return ((IMixinEntityLivingBase) getMinecraftEntity()).getActiveItemStackUseCount(); // TODO
	}

	public void doJump() {
		getMinecraftEntity().jumpFromGround();
	}

	public int getItemInUseMaxCount() {
		return getMinecraftEntity().getUseItemRemainingTicks();
	}

	public static final double PLAYER_WIDTH = 49, PLAYER_HEIGHT = 70, DEFAULT_SIZE = 30;
	public static double PLAYER_WIDTH_MP = PLAYER_WIDTH / DEFAULT_SIZE;
	public static double PLAYER_HEIGHT_MP = PLAYER_HEIGHT / DEFAULT_SIZE;

	public void drawPlayer(GLX context, int posX, int posY, int size) {
		int width = (int) (size * PLAYER_WIDTH_MP);
		int height = (int) (size * PLAYER_HEIGHT_MP);

		InventoryScreen.extractEntityInInventoryFollowsMouse(context.getContext(), posX, posY,
				posX + width, posY + height,
				size, 0.0625F, posX + width / 2f,
				posY + height / 2f, getMinecraftEntity());
	}

	public Entity clone() {
		return Entity.newInstance(new ClonedPlayerMP(getMinecraftEntity()));
	}

	public float getFlySpeed() {
		return getMinecraftEntity().getAbilities().getFlyingSpeed();
	}

	public void setFlySpeed(float speed) {
		getMinecraftEntity().getAbilities().setFlyingSpeed(speed);
	}

	public float getWalkSpeed() {
		return getMinecraftEntity().getAbilities().getWalkingSpeed();
	}

	public void setWalkSpeed(float speed) {
		getMinecraftEntity().getAbilities().setWalkingSpeed(speed);
	}

}
