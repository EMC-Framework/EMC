package me.deftware.client.framework.wrappers.entity;

import me.deftware.client.framework.wrappers.item.IItemStack;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.math.IVec3d;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import me.deftware.client.framework.wrappers.world.IEnumFacing;
import me.deftware.client.framework.wrappers.world.IWorld;
import me.deftware.mixin.imp.IMixinEntity;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("All")
public class IEntityPlayer {

	private static ScheduledFuture<?> pingThread = null;

	private static int ping = 0;
	private static long previousTotalWorldTime;
	private static double previousMeasureTime, currentTPS = 0;

	public static void drawPlayer(int posX, int posY, int scale) {
		if (Minecraft.getMinecraft().player != null) {
		    GuiInventory.drawEntityOnScreen(posX, posY, scale, 0, 0, Minecraft.getMinecraft().player);
		}
	}

	private static IPlayer playerCache;

	public static IPlayer getIPlayer() {
		if (playerCache == null || playerCache.getEntityID() != Minecraft.getMinecraft().player.getEntityId()) {
			playerCache = (IPlayer) IEntity.fromEntity(Minecraft.getMinecraft().player);
		}
		return playerCache;
	}

	public static boolean isAtEdge() {
		return Minecraft.getMinecraft().world.getCollisionBoxes(Minecraft.getMinecraft().player, Minecraft.getMinecraft().player.getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty();
	}

	public static boolean processRightClickBlock(IBlockPos pos, IEnumFacing facing, IVec3d vec) {
		return Minecraft.getMinecraft().playerController.processRightClickBlock(Minecraft.getMinecraft().player,
				Minecraft.getMinecraft().world, pos.getPos(), IEnumFacing.getFacing(facing), vec.getVector(),
				EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS;
	}


	public static void doJump() {
		Minecraft.getMinecraft().player.jump();
	}

	public static IItemStack getHeldItem(boolean offset) {
		ItemStack stack = Minecraft.getMinecraft().player.inventory.getCurrentItem();
		if (offset) {
			stack = Minecraft.getMinecraft().player.getHeldItemOffhand();
		}
		if (stack == null) {
			return null;
		}
		return new IItemStack(stack);
	}

	public static float getStepHeight() {
		return Minecraft.getMinecraft().player.stepHeight;
	}

	public static void setStepHeight(float height) {
		Minecraft.getMinecraft().player.stepHeight = height;
	}

	public static IEntity getRidingEntity() {
		if (Minecraft.getMinecraft().player.getRidingEntity() != null) {
			return IEntity.fromEntity(Minecraft.getMinecraft().player.getRidingEntity());
		}
		return null;
	}

	public static IDirection getRidingEntityDirection() {
		if (getRidingEntity() == null) {
			return IDirection.NORTH;
		}
		return getRidingEntity().getDirection();
	}

	public static float getRidingEntityRotationYaw() {
		if (getRidingEntity() == null) {
			return 0;
		}
		return getRidingEntity().getEntity().rotationYaw;
	}

	public static void setRidingEntityRotationYaw(float yaw) {
		if (getRidingEntity() == null) {
			return;
		}
		getRidingEntity().getEntity().rotationYaw = yaw;
	}

	public static float getRidingEntityRotationPitch() {
		if (getRidingEntity() == null) {
			return 0;
		}
		return getRidingEntity().getEntity().rotationPitch;
	}

	public static void setRidingEntityRotationPitch(float pitch) {
		if (getRidingEntity() == null) {
			return;
		}
		getRidingEntity().getEntity().rotationPitch = pitch;
	}

	public static int getFoodLevel() {
		return Minecraft.getMinecraft().player.getFoodStats().getFoodLevel();
	}

	public static IEntity clonePlayer() {
		return IEntity.fromEntity(new IEntityOtherPlayerMP());
	}

	public static boolean isAirBorne() {
		return Minecraft.getMinecraft().player.isAirBorne;
	}

	public static boolean getFlag(int flag) {
		return ((IMixinEntity) Minecraft.getMinecraft().player).getAFlag(flag);
	}

	public static void setInPortal(boolean inPortal) {
		((IMixinEntity) Minecraft.getMinecraft().player).setInPortal(inPortal);
	}

	public static void setSprinting(boolean state) {
		Minecraft.getMinecraft().player.setSprinting(state);
	}

	public static boolean isSprinting() {
		return Minecraft.getMinecraft().player.isSprinting();
	}

	public static float getMoveStrafing() {
		return Minecraft.getMinecraft().player.moveStrafing;
	}

	public static float getMoveForward() {
		return Minecraft.getMinecraft().player.moveForward;
	}

	public static boolean isCollidedHorizontally() {
		return Minecraft.getMinecraft().player.collidedHorizontally;
	}

	public static boolean isRidingEntityInWater() {
		return Minecraft.getMinecraft().player.getRidingEntity().isInWater();
	}

	public static double getRidingEntityMotionX() {
		return Minecraft.getMinecraft().player.getRidingEntity().motionX;
	}

	public static double getRidingEntityMotionY() {
		return Minecraft.getMinecraft().player.getRidingEntity().motionY;
	}

	public static double getRidingEntityMotionZ() {
		return Minecraft.getMinecraft().player.getRidingEntity().motionZ;
	}

	public static int getHurtTime() {
		return Minecraft.getMinecraft().player.hurtTime;
	}

	public static void ridingEntityMotionY(double y) {
		Minecraft.getMinecraft().player.getRidingEntity().motionY = y;
	}

	public static void ridingEntityMotionX(double x) {
		Minecraft.getMinecraft().player.getRidingEntity().motionX = x;
	}

	public static void ridingEntityMotionZ(double z) {
		Minecraft.getMinecraft().player.getRidingEntity().motionZ = z;
	}

	public static void ridingEntityMotionTimesY(double y) {
		Minecraft.getMinecraft().player.getRidingEntity().motionY *= y;
	}

	public static void ridingEntityMotionTimesX(double x) {
		Minecraft.getMinecraft().player.getRidingEntity().motionX *= x;
	}

	public static void ridingEntityMotionTimesZ(double z) {
		Minecraft.getMinecraft().player.getRidingEntity().motionZ *= z;
	}

	public static boolean isRidingOnGround() {
		return Minecraft.getMinecraft().player.getRidingEntity().onGround;
	}

	public static void attackEntity(IEntity entity) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, entity.getEntity());
		IEntityPlayer.swingArmClientSide();
	}

	public static boolean isCreative() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().player.isCreative();
	}

	public static void setPositionX(int x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.setPosition(Minecraft.getMinecraft().player.posX + x,
				Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ);
	}

	public static void setPositionY(int y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.setPosition(Minecraft.getMinecraft().player.posX,
				Minecraft.getMinecraft().player.posY + y, Minecraft.getMinecraft().player.posZ);
	}

	public static void setPositionZ(int z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.setPosition(Minecraft.getMinecraft().player.posX,
				Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ + z);
	}

	public static void setPosition(double x, double y, double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.setPosition(x, y, z);
	}

	public static void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.setPositionAndRotation(x, y, z, yaw, pitch);
	}

	public static void setJumpMovementFactor(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.jumpMovementFactor = speed;
	}

	public static void setJumpMovementFactorTimes(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.jumpMovementFactor *= speed;
	}

	public static void setNoClip(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.noClip = state;
	}

	public static void setFalldistance(float distance) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.fallDistance = distance;
	}

	public static double getMotionX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.motionX;
	}

	public static void setMotionX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionX = x;
	}

	public static double getMotionY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.motionY;
	}

	public static void setMotionY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionY = y;
	}

	public static double getMotionZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.motionZ;
	}

	public static void setMotionZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionZ = z;
	}

	public static void setMotionTimesX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionX *= x;
	}

	public static void setMotionTimesY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionY *= y;
	}

	public static void setMotionTimesZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionZ *= z;
	}

	public static void setMotionPlusX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionX += x;
	}

	public static void setMotionPlusY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionY += y;
	}

	public static void setMotionPlusZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionZ += z;
	}

	public static void setMotionMinusX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionX -= x;
	}

	public static void setMotionMinusY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionY -= y;
	}

	public static void setMotionMinusZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.motionZ -= z;
	}

	public static void respawnPlayer() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.respawnPlayer();
	}

	public static void swingArmClientSide() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
	}

	public static float getSaturationLevel() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.getFoodStats().getSaturationLevel();
	}

	public static void swingArmPacket() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
	}

	public static float getCooldown() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.getCooledAttackStrength(0);
	}

	public static IDirection getDirection() {
		if (IEntityPlayer.isNull()) {
			return IDirection.NORTH;
		}
		return IDirection.getFrom(getRotationYaw(true));
	}

	public static float getRotationYaw(boolean fullCircleCalc) {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		float currentYaw = Minecraft.getMinecraft().player.rotationYaw % 360;

		if (fullCircleCalc) {
			currentYaw = (Minecraft.getMinecraft().player.rotationYaw + 90) % 360;
			if (currentYaw < 0) {
				currentYaw += 360;
			}
		} else if (currentYaw > 180) {
			currentYaw -= 360;
		}

		return currentYaw;
	}

	public static float getRotationYaw() {
		return getRotationYaw(false);
	}

	public static void setRotationYaw(float yaw) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.rotationYaw = yaw;
	}

	public static float getRotationPitch() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.rotationPitch;
	}

	public static void setRotationPitch(float pitch) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.rotationPitch = pitch;
	}

	public static double getPosX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.posX;
	}

	public static double getPosY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.posY;
	}

	public static double getPosZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.posZ;
	}

	public static double getEyeHeight() {
		return Minecraft.getMinecraft().player.getEyeHeight();
	}

	public static double getEyeHeight(Object pose) {
		return Minecraft.getMinecraft().player.getEyeHeight();
	}

	public static int getItemInUseMaxCount() {
		return Minecraft.getMinecraft().player.getItemInUseMaxCount();
	}

	public static double getPrevPosX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.prevPosX;
	}

	public static double getPrevPosY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.prevPosY;
	}

	public static double getPrevPosZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.prevPosZ;
	}

	public static float getHealth() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.getHealth();
	}

	public static float getFallDistance() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.fallDistance;
	}

	public static boolean hasPotionEffects() {
		return !Minecraft.getMinecraft().player.getActivePotionEffects().isEmpty();
	}

	public static boolean isSingleplayer() {
		if (IEntityPlayer.isNull()) {
			return true;
		}
		return Minecraft.getMinecraft().isSingleplayer();
	}

	public static String getDisplayX() {
		if (IEntityPlayer.isNull()) {
			return "0";
		}
		return String.format("%.3f",
				Minecraft.getMinecraft().getRenderViewEntity().posX);
	}

	public static String getDisplayY() {
		if (IEntityPlayer.isNull()) {
			return "0";
		}
		return String.format("%.5f",
				Minecraft.getMinecraft().getRenderViewEntity().posY);
	}

	public static String getDisplayZ() {
		if (IEntityPlayer.isNull()) {
			return "0";
		}
		return String.format("%.3f",
				Minecraft.getMinecraft().getRenderViewEntity().posZ);
	}

	/**
	 * Which dimension the player is in (-1 = the Nether, 0 = normal world)
	 *
	 * @return The dimension
	 */
	public static int getDimension() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().player.dimension;
	}

	public static boolean isRowingBoat() {
		if (IEntityPlayer.isNull()) {
			return false;
		} else return Minecraft.getMinecraft().player.getRidingEntity() instanceof EntityBoat;
	}

	public static boolean isRiding() {
		return Minecraft.getMinecraft().player.isRiding();
	}

	public static boolean isRidingHorse() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().player.isRidingHorse() && Minecraft.getMinecraft().player.getRidingEntity() instanceof AbstractHorse;
	}

	public static boolean isInLiquid() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().player.isInWater() || Minecraft.getMinecraft().player.isInLava();
	}

	public static boolean isFlying() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().player.capabilities.isFlying;
	}

	public static void setFlying(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.capabilities.isFlying = state;
	}

	public static float getFlySpeed() {
		if (IEntityPlayer.isNull()) {
			return 0F;
		}
		return Minecraft.getMinecraft().player.capabilities.getFlySpeed();
	}

	public static void setFlySpeed(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.capabilities.setFlySpeed(speed);
	}

	public static float getWalkSpeed() {
		if (IEntityPlayer.isNull()) {
			return 0F;
		}
		return Minecraft.getMinecraft().player.capabilities.getWalkSpeed();
	}

	public static void setWalkSpeed(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.capabilities.setPlayerWalkSpeed(speed);
	}

	public static String getName() {
		if (IEntityPlayer.isNull()) {
			return "";
		}
		return Minecraft.getMinecraft().player.getGameProfile().getName();
	}

	public static boolean isOnGround() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().player.onGround;
	}

	public static void setOnGround(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().player.onGround = state;
	}

	public static boolean isOnLadder() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().player.isOnLadder();
	}

	public static boolean isNull() {
		return Minecraft.getMinecraft().player == null;
	}

	public static boolean isHoldingItem(HandItem item) {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		if (item.equals(HandItem.ItemBow)) {
			return Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() instanceof ItemBow
					|| Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() instanceof ItemBow;
		}
		return false;
	}

	public static boolean isSneaking() {
		return Minecraft.getMinecraft().player.isSneaking();
	}

	public static boolean isInAir() {
		return Minecraft.getMinecraft().player.isInsideOfMaterial(Material.AIR);
	}

	public static IAxisAlignedBB getBoundingBox() {
		return new IAxisAlignedBB(Minecraft.getMinecraft().player.getEntityBoundingBox());
	}

	public static boolean isTouchingLiquid() {
		Minecraft mc = Minecraft.getMinecraft();
		boolean inLiquid = false;
		int y = (int) mc.player.getEntityBoundingBox().minY;
		for (int x = IEntityPlayer.floor_double(mc.player.getEntityBoundingBox().minX); x < IEntityPlayer.floor_double(mc.player.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = IEntityPlayer.floor_double(mc.player.getEntityBoundingBox().minZ); z < IEntityPlayer.floor_double(mc.player.getEntityBoundingBox().maxZ)
					+ 1; z++) {
				net.minecraft.block.Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static int getPing() {
		if (pingThread == null || pingThread.isCancelled() || pingThread.isDone()) {
			pingThread = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
				try {
					if (!IEntityPlayer.isNull() && !IWorld.isNull()) {
						NetHandlerPlayClient nethandlerplayclient = Minecraft.getMinecraft().player.connection;
						ping = nethandlerplayclient.getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID()).getResponseTime();
					} else {
                        ping = 0;
                    }
				} catch (Throwable ignored) { // It can be unstable at times
                    ping = 0;
				}
			}, 0, 5, TimeUnit.SECONDS);
		}
		return ping;
	}

	/**
	 * Updates the TPS variable
	 *
	 * @return the Current TPS, as a rounded string
	 */
	public static String getTPS() {
		if (!IWorld.isNull()) {
			if (getTimeInSeconds() - previousMeasureTime < 3.0) {
				// Parse Existing TPS
				DecimalFormat df = new DecimalFormat("#.##");
				df.setRoundingMode(RoundingMode.CEILING);
				return df.format(currentTPS);
			}
			currentTPS = ((double) (IWorld.getWorldTime() - previousTotalWorldTime)) / (getTimeInSeconds() - previousMeasureTime);

			// Limits TPS to not go above 20, sometimes possible
			if (currentTPS > 20.0d) {
				currentTPS = 20.0d;
			}
			// Also prevent it going below 20, which is also sometimes possible
			if (currentTPS < 0.0d) {
				currentTPS = 0.0d;
			}

			updatePreviousTotalWorldTime();
		} else {
			currentTPS = 0.0d;
		}

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		return df.format(currentTPS);
	}

	/**
	 * Retrieves the Raw TPS, in Double Format
	 */
	public static double getRawTPS() {
		return currentTPS;
	}

	/**
	 * Sets the TotalWorldTime to whatever it is at the moment, effectively updating it
	 */
	private static void updatePreviousTotalWorldTime() {
		try {
			if (IWorld.getWorldTime() == 0) {
				Thread.sleep(500);

				if (IWorld.getWorldTime() == 0) {
					Thread.sleep(1000);
					if (IWorld.getWorldTime() == 0) {
						System.out.println("The TotalWorldTime is 0 after waiting for the TotalWorldTime, and should not happen!");
					}
				}
			}
			previousTotalWorldTime = IWorld.getWorldTime();
			previousMeasureTime = getTimeInSeconds();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the Current Time, in seconds
	 *
	 * @return the current time, in seconds
	 */
	public static double getTimeInSeconds() {
		return (System.currentTimeMillis() / 1000d);
	}

	public static int floor_double(double value) {
		int i = (int) value;
		return value < i ? i - 1 : i;
	}

	public static void toggleSkinLayers() {
		Set<?> activeParts = Minecraft.getMinecraft().gameSettings.getModelParts();
		for (EnumPlayerModelParts part : EnumPlayerModelParts.values()) {
			Minecraft.getMinecraft().gameSettings.setModelPartEnabled(part, !activeParts.contains(part));
		}
	}

	public static IBlockPos getPos() {
		return new IBlockPos(IEntityPlayer.getPosX(), IEntityPlayer.getPosY(), IEntityPlayer.getPosZ());
	}

	public static void setHorseJumpPower(float f) {
		((IMixinEntityPlayerSP) Minecraft.getMinecraft().player).setHorseJumpPower(f);
	}

	public static boolean isAlive() {
		return Minecraft.getMinecraft().player.isEntityAlive();
	}

	public static void setAlive(boolean flag) {
		Minecraft.getMinecraft().player.isDead = false;
		Minecraft.getMinecraft().player.setHealth(20f);
		Minecraft.getMinecraft().player.setPosition(getPosX(), getPosY(), getPosZ());
	}

	public enum HandItem {
		ItemBow
	}

}
