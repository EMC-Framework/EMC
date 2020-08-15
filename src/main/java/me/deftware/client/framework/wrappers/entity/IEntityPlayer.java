package me.deftware.client.framework.wrappers.entity;

import me.deftware.client.framework.wrappers.item.IItem;
import me.deftware.client.framework.wrappers.item.IItemStack;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.math.IVec3d;
import me.deftware.client.framework.wrappers.world.IBlock;
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
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;

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
		if (Minecraft.getMinecraft().thePlayer != null) {
		    GuiInventory.drawEntityOnScreen(posX, posY, scale, 0, 0, Minecraft.getMinecraft().thePlayer);
		}
	}

	private static IPlayer playerCache;

	public static IPlayer getIPlayer() {
		if (playerCache == null || playerCache.getEntityID() != Minecraft.getMinecraft().thePlayer.getEntityId()) {
			playerCache = (IPlayer) IEntity.fromEntity(Minecraft.getMinecraft().thePlayer);
		}
		return playerCache;
	}

	public static boolean isAtEdge() {
		return Minecraft.getMinecraft().theWorld.getCollisionBoxes(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty();
	}

	public static boolean processRightClickBlock(IBlockPos pos, IEnumFacing facing, IVec3d vec) {
		return Minecraft.getMinecraft().playerController.onPlayerRightClick(Minecraft.getMinecraft().thePlayer,
				Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem(), pos.getPos(), IEnumFacing.getFacing(facing), vec.vector);
	}


	public static void doJump() {
		Minecraft.getMinecraft().thePlayer.jump();
	}

	public static IItemStack getHeldItem(boolean offset) {
		ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
		if (offset) {
			//stack = Minecraft.getMinecraft().thePlayer.getHeldItemOffhand();
		}
		if (stack == null) {
			return new IItemStack(new IBlock(Blocks.air));
		}
		return new IItemStack(stack);
	}

	public static float getStepHeight() {
		return Minecraft.getMinecraft().thePlayer.stepHeight;
	}

	public static void setStepHeight(float height) {
		Minecraft.getMinecraft().thePlayer.stepHeight = height;
	}

	public static IEntity getRidingEntity() {
		if (Minecraft.getMinecraft().thePlayer.ridingEntity != null) {
			return IEntity.fromEntity(Minecraft.getMinecraft().thePlayer.ridingEntity);
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
		return Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel();
	}

	public static IEntity clonePlayer() {
		return IEntity.fromEntity(new IEntityOtherPlayerMP());
	}

	public static boolean isAirBorne() {
		return Minecraft.getMinecraft().thePlayer.isAirBorne;
	}

	public static boolean getFlag(int flag) {
		return ((IMixinEntity) Minecraft.getMinecraft().thePlayer).getAFlag(flag);
	}

	public static void setInPortal(boolean inPortal) {
		((IMixinEntity) Minecraft.getMinecraft().thePlayer).setInPortal(inPortal);
	}

	public static void setSprinting(boolean state) {
		Minecraft.getMinecraft().thePlayer.setSprinting(state);
	}

	public static boolean isSprinting() {
		return Minecraft.getMinecraft().thePlayer.isSprinting();
	}

	public static float getMoveStrafing() {
		return Minecraft.getMinecraft().thePlayer.moveStrafing;
	}

	public static float getMoveForward() {
		return Minecraft.getMinecraft().thePlayer.moveForward;
	}

	public static boolean isCollidedHorizontally() {
		return Minecraft.getMinecraft().thePlayer.isCollidedHorizontally;
	}

	public static boolean isRidingEntityInWater() {
		return Minecraft.getMinecraft().thePlayer.ridingEntity.isInWater();
	}

	public static double getRidingEntityMotionX() {
		return Minecraft.getMinecraft().thePlayer.ridingEntity.motionX;
	}

	public static double getRidingEntityMotionY() {
		return Minecraft.getMinecraft().thePlayer.ridingEntity.motionY;
	}

	public static double getRidingEntityMotionZ() {
		return Minecraft.getMinecraft().thePlayer.ridingEntity.motionZ;
	}

	public static int getHurtTime() {
		return Minecraft.getMinecraft().thePlayer.hurtTime;
	}

	public static void ridingEntityMotionY(double y) {
		Minecraft.getMinecraft().thePlayer.ridingEntity.motionY = y;
	}

	public static void ridingEntityMotionX(double x) {
		Minecraft.getMinecraft().thePlayer.ridingEntity.motionX = x;
	}

	public static void ridingEntityMotionZ(double z) {
		Minecraft.getMinecraft().thePlayer.ridingEntity.motionZ = z;
	}

	public static void ridingEntityMotionTimesY(double y) {
		Minecraft.getMinecraft().thePlayer.ridingEntity.motionY *= y;
	}

	public static void ridingEntityMotionTimesX(double x) {
		Minecraft.getMinecraft().thePlayer.ridingEntity.motionX *= x;
	}

	public static void ridingEntityMotionTimesZ(double z) {
		Minecraft.getMinecraft().thePlayer.ridingEntity.motionZ *= z;
	}

	public static boolean isRidingOnGround() {
		return Minecraft.getMinecraft().thePlayer.ridingEntity.onGround;
	}

	public static void attackEntity(IEntity entity) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, entity.getEntity());
		IEntityPlayer.swingArmClientSide();
	}

	public static boolean isCreative() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().playerController.isInCreativeMode();
	}

	public static void setPositionX(int x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX + x,
				Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);
	}

	public static void setPositionY(int y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX,
				Minecraft.getMinecraft().thePlayer.posY + y, Minecraft.getMinecraft().thePlayer.posZ);
	}

	public static void setPositionZ(int z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX,
				Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ + z);
	}

	public static void setPosition(double x, double y, double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.setPosition(x, y, z);
	}

	public static void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.setPositionAndRotation(x, y, z, yaw, pitch);
	}

	public static void setJumpMovementFactor(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.jumpMovementFactor = speed;
	}

	public static void setJumpMovementFactorTimes(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.jumpMovementFactor *= speed;
	}

	public static void setNoClip(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.noClip = state;
	}

	public static void setFalldistance(float distance) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.fallDistance = distance;
	}

	public static double getMotionX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.motionX;
	}

	public static void setMotionX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionX = x;
	}

	public static double getMotionY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.motionY;
	}

	public static void setMotionY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionY = y;
	}

	public static double getMotionZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.motionZ;
	}

	public static void setMotionZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionZ = z;
	}

	public static void setMotionTimesX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionX *= x;
	}

	public static void setMotionTimesY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionY *= y;
	}

	public static void setMotionTimesZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionZ *= z;
	}

	public static void setMotionPlusX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionX += x;
	}

	public static void setMotionPlusY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionY += y;
	}

	public static void setMotionPlusZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionZ += z;
	}

	public static void setMotionMinusX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionX -= x;
	}

	public static void setMotionMinusY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionY -= y;
	}

	public static void setMotionMinusZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.motionZ -= z;
	}

	public static void respawnPlayer() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.respawnPlayer();
	}

	public static void swingArmClientSide() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.swingItem();
	}

	public static float getSaturationLevel() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.getFoodStats().getSaturationLevel();
	}

	public static void swingArmPacket() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	}

	public static float getCooldown() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return 1;
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
		float currentYaw = Minecraft.getMinecraft().thePlayer.rotationYaw % 360;

		if (fullCircleCalc) {
			currentYaw = (Minecraft.getMinecraft().thePlayer.rotationYaw + 90) % 360;
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
		Minecraft.getMinecraft().thePlayer.rotationYaw = yaw;
	}

	public static float getRotationPitch() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.rotationPitch;
	}

	public static void setRotationPitch(float pitch) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.rotationPitch = pitch;
	}

	public static double getPosX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.posX;
	}

	public static double getPosY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.posY;
	}

	public static double getPosZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.posZ;
	}

	public static double getEyeHeight() {
		return Minecraft.getMinecraft().thePlayer.getEyeHeight();
	}

	public static double getEyeHeight(Object pose) {
		return Minecraft.getMinecraft().thePlayer.getEyeHeight();
	}

	public static int getItemInUseMaxCount() {
		return Minecraft.getMinecraft().thePlayer.getHeldItem().getMaxItemUseDuration();
	}

	public static double getPrevPosX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.prevPosX;
	}

	public static double getPrevPosY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.prevPosY;
	}

	public static double getPrevPosZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.prevPosZ;
	}

	public static float getHealth() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.getHealth();
	}

	public static float getFallDistance() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getMinecraft().thePlayer.fallDistance;
	}

	public static boolean hasPotionEffects() {
		return !Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty();
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
		return Minecraft.getMinecraft().thePlayer.dimension;
	}

	public static boolean isRowingBoat() {
		if (IEntityPlayer.isNull()) {
			return false;
		} else return Minecraft.getMinecraft().thePlayer.ridingEntity instanceof EntityBoat;
	}

	public static boolean isRiding() {
		return Minecraft.getMinecraft().thePlayer.isRiding();
	}

	public static boolean isRidingHorse() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().thePlayer.isRidingHorse() && Minecraft.getMinecraft().thePlayer.ridingEntity instanceof EntityHorse;
	}

	public static boolean isInLiquid() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().thePlayer.isInWater() || Minecraft.getMinecraft().thePlayer.isInLava();
	}

	public static boolean isFlying() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().thePlayer.capabilities.isFlying;
	}

	public static void setFlying(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.capabilities.isFlying = state;
	}

	public static float getFlySpeed() {
		if (IEntityPlayer.isNull()) {
			return 0F;
		}
		return Minecraft.getMinecraft().thePlayer.capabilities.getFlySpeed();
	}

	public static void setFlySpeed(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.capabilities.setFlySpeed(speed);
	}

	public static float getWalkSpeed() {
		if (IEntityPlayer.isNull()) {
			return 0F;
		}
		return Minecraft.getMinecraft().thePlayer.capabilities.getWalkSpeed();
	}

	public static void setWalkSpeed(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.capabilities.setPlayerWalkSpeed(speed);
	}

	public static String getName() {
		if (IEntityPlayer.isNull()) {
			return "";
		}
		return Minecraft.getMinecraft().thePlayer.getGameProfile().getName();
	}

	public static boolean isOnGround() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().thePlayer.onGround;
	}

	public static void setOnGround(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getMinecraft().thePlayer.onGround = state;
	}

	public static boolean isOnLadder() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getMinecraft().thePlayer.isOnLadder();
	}

	public static boolean isNull() {
		return Minecraft.getMinecraft().thePlayer == null;
	}

	public static boolean isHoldingItem(HandItem item) {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		if (item.equals(HandItem.ItemBow)) {
			return Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemBow;
		}
		return false;
	}

	public static boolean isSneaking() {
		return Minecraft.getMinecraft().thePlayer.isSneaking();
	}

	public static boolean isInAir() {
		return Minecraft.getMinecraft().thePlayer.isInsideOfMaterial(Material.air);
	}

	public static IAxisAlignedBB getBoundingBox() {
		return new IAxisAlignedBB(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox());
	}

	public static boolean isTouchingLiquid() {
		Minecraft mc = Minecraft.getMinecraft();
		boolean inLiquid = false;
		int y = (int) mc.thePlayer.getEntityBoundingBox().minY;
		for (int x = IEntityPlayer.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < IEntityPlayer.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = IEntityPlayer.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < IEntityPlayer.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ)
					+ 1; z++) {
				net.minecraft.block.Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
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
						NetHandlerPlayClient nethandlerplayclient = Minecraft.getMinecraft().thePlayer.sendQueue;
						ping = nethandlerplayclient.getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID()).getResponseTime();
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
		((IMixinEntityPlayerSP) Minecraft.getMinecraft().thePlayer).setHorseJumpPower(f);
	}

	public static boolean isAlive() {
		return Minecraft.getMinecraft().thePlayer.isEntityAlive();
	}

	public static void setAlive(boolean flag) {
		Minecraft.getMinecraft().thePlayer.isDead = false;
		Minecraft.getMinecraft().thePlayer.setHealth(20f);
		Minecraft.getMinecraft().thePlayer.setPosition(getPosX(), getPosY(), getPosZ());
	}

	public enum HandItem {
		ItemBow
	}

}
