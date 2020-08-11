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
import net.minecraft.block.BlockFlowingFluid;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
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
		if (Minecraft.getInstance().player != null) {
		    GuiInventory.drawEntityOnScreen(posX, posY, scale, 0, 0, Minecraft.getInstance().player);
		}
	}

	private static IPlayer playerCache;

	public static IPlayer getIPlayer() {
		if (playerCache == null || playerCache.getEntityID() != Minecraft.getInstance().player.getEntityId()) {
			playerCache = (IPlayer) IEntity.fromEntity(Minecraft.getInstance().player);
		}
		return playerCache;
	}

	public static boolean isAtEdge() {
		return Minecraft.getInstance().world.getCollisionBoxes(Minecraft.getInstance().player, Minecraft.getInstance().player.getBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty();
	}

	public static boolean processRightClickBlock(IBlockPos pos, IEnumFacing facing, IVec3d vec) {
		return Minecraft.getInstance().playerController.processRightClickBlock(Minecraft.getInstance().player,
				Minecraft.getInstance().world, pos.getPos(), IEnumFacing.getFacing(facing), vec.getVector(),
				EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS;
	}


	public static void doJump() {
		Minecraft.getInstance().player.jump();
	}

	public static IItemStack getHeldItem(boolean offset) {
		ItemStack stack = Minecraft.getInstance().player.inventory.getCurrentItem();
		if (offset) {
			stack = Minecraft.getInstance().player.getHeldItemOffhand();
		}
		if (stack == null) {
			return null;
		}
		return new IItemStack(stack);
	}

	public static float getStepHeight() {
		return Minecraft.getInstance().player.stepHeight;
	}

	public static void setStepHeight(float height) {
		Minecraft.getInstance().player.stepHeight = height;
	}

	public static IEntity getRidingEntity() {
		if (Minecraft.getInstance().player.getRidingEntity() != null) {
			return IEntity.fromEntity(Minecraft.getInstance().player.getRidingEntity());
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
		return Minecraft.getInstance().player.getFoodStats().getFoodLevel();
	}

	public static IEntity clonePlayer() {
		return IEntity.fromEntity(new IEntityOtherPlayerMP());
	}

	public static boolean isAirBorne() {
		return Minecraft.getInstance().player.isAirBorne;
	}

	public static boolean getFlag(int flag) {
		return ((IMixinEntity) Minecraft.getInstance().player).getAFlag(flag);
	}

	public static void setInPortal(boolean inPortal) {
		((IMixinEntity) Minecraft.getInstance().player).setInPortal(inPortal);
	}

	public static void setSprinting(boolean state) {
		Minecraft.getInstance().player.setSprinting(state);
	}

	public static boolean isSprinting() {
		return Minecraft.getInstance().player.isSprinting();
	}

	public static float getMoveStrafing() {
		return Minecraft.getInstance().player.moveStrafing;
	}

	public static float getMoveForward() {
		return Minecraft.getInstance().player.moveForward;
	}

	public static boolean isCollidedHorizontally() {
		return Minecraft.getInstance().player.collidedHorizontally;
	}

	public static boolean isRidingEntityInWater() {
		return Minecraft.getInstance().player.getRidingEntity().isInWater();
	}

	public static double getRidingEntityMotionX() {
		return Minecraft.getInstance().player.getRidingEntity().motionX;
	}

	public static double getRidingEntityMotionY() {
		return Minecraft.getInstance().player.getRidingEntity().motionY;
	}

	public static double getRidingEntityMotionZ() {
		return Minecraft.getInstance().player.getRidingEntity().motionZ;
	}

	public static int getHurtTime() {
		return Minecraft.getInstance().player.hurtTime;
	}

	public static void ridingEntityMotionY(double y) {
		Minecraft.getInstance().player.getRidingEntity().motionY = y;
	}

	public static void ridingEntityMotionX(double x) {
		Minecraft.getInstance().player.getRidingEntity().motionX = x;
	}

	public static void ridingEntityMotionZ(double z) {
		Minecraft.getInstance().player.getRidingEntity().motionZ = z;
	}

	public static void ridingEntityMotionTimesY(double y) {
		Minecraft.getInstance().player.getRidingEntity().motionY *= y;
	}

	public static void ridingEntityMotionTimesX(double x) {
		Minecraft.getInstance().player.getRidingEntity().motionX *= x;
	}

	public static void ridingEntityMotionTimesZ(double z) {
		Minecraft.getInstance().player.getRidingEntity().motionZ *= z;
	}

	public static boolean isRidingOnGround() {
		return Minecraft.getInstance().player.getRidingEntity().onGround;
	}

	public static void attackEntity(IEntity entity) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().playerController.attackEntity(Minecraft.getInstance().player, entity.getEntity());
		IEntityPlayer.swingArmClientSide();
	}

	public static boolean isCreative() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getInstance().player.isCreative();
	}

	public static void setPositionX(int x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.setPosition(Minecraft.getInstance().player.posX + x,
				Minecraft.getInstance().player.posY, Minecraft.getInstance().player.posZ);
	}

	public static void setPositionY(int y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.setPosition(Minecraft.getInstance().player.posX,
				Minecraft.getInstance().player.posY + y, Minecraft.getInstance().player.posZ);
	}

	public static void setPositionZ(int z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.setPosition(Minecraft.getInstance().player.posX,
				Minecraft.getInstance().player.posY, Minecraft.getInstance().player.posZ + z);
	}

	public static void setPosition(double x, double y, double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.setPosition(x, y, z);
	}

	public static void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.setPositionAndRotation(x, y, z, yaw, pitch);
	}

	public static void setJumpMovementFactor(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.jumpMovementFactor = speed;
	}

	public static void setJumpMovementFactorTimes(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.jumpMovementFactor *= speed;
	}

	public static void setNoClip(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.noClip = state;
	}

	public static void setFalldistance(float distance) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.fallDistance = distance;
	}

	public static double getMotionX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.motionX;
	}

	public static void setMotionX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionX = x;
	}

	public static double getMotionY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.motionY;
	}

	public static void setMotionY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionY = y;
	}

	public static double getMotionZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.motionZ;
	}

	public static void setMotionZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionZ = z;
	}

	public static void setMotionTimesX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionX *= x;
	}

	public static void setMotionTimesY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionY *= y;
	}

	public static void setMotionTimesZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionZ *= z;
	}

	public static void setMotionPlusX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionX += x;
	}

	public static void setMotionPlusY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionY += y;
	}

	public static void setMotionPlusZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionZ += z;
	}

	public static void setMotionMinusX(double x) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionX -= x;
	}

	public static void setMotionMinusY(double y) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionY -= y;
	}

	public static void setMotionMinusZ(double z) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.motionZ -= z;
	}

	public static void respawnPlayer() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.respawnPlayer();
	}

	public static void swingArmClientSide() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.swingArm(EnumHand.MAIN_HAND);
	}

	public static float getSaturationLevel() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.getFoodStats().getSaturationLevel();
	}

	public static void swingArmPacket() {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
	}

	public static float getCooldown() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.getCooledAttackStrength(0);
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
		float currentYaw = Minecraft.getInstance().player.rotationYaw % 360;

		if (fullCircleCalc) {
			currentYaw = (Minecraft.getInstance().player.rotationYaw + 90) % 360;
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
		Minecraft.getInstance().player.rotationYaw = yaw;
	}

	public static float getRotationPitch() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.rotationPitch;
	}

	public static void setRotationPitch(float pitch) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.rotationPitch = pitch;
	}

	public static double getPosX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.posX;
	}

	public static double getPosY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.posY;
	}

	public static double getPosZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.posZ;
	}

	public static double getEyeHeight() {
		return Minecraft.getInstance().player.getEyeHeight();
	}

	public static double getEyeHeight(Object pose) {
		return Minecraft.getInstance().player.getEyeHeight();
	}

	public static int getItemInUseMaxCount() {
		return Minecraft.getInstance().player.getItemInUseMaxCount();
	}

	public static double getPrevPosX() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.prevPosX;
	}

	public static double getPrevPosY() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.prevPosY;
	}

	public static double getPrevPosZ() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.prevPosZ;
	}

	public static float getHealth() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.getHealth();
	}

	public static float getFallDistance() {
		if (IEntityPlayer.isNull()) {
			return 0;
		}
		return Minecraft.getInstance().player.fallDistance;
	}

	public static boolean hasPotionEffects() {
		return !Minecraft.getInstance().player.getActivePotionEffects().isEmpty();
	}

	public static boolean isSingleplayer() {
		if (IEntityPlayer.isNull()) {
			return true;
		}
		return Minecraft.getInstance().isSingleplayer();
	}

	public static String getDisplayX() {
		if (IEntityPlayer.isNull()) {
			return "0";
		}
		return String.format("%.3f",
				Minecraft.getInstance().getRenderViewEntity().posX);
	}

	public static String getDisplayY() {
		if (IEntityPlayer.isNull()) {
			return "0";
		}
		return String.format("%.5f",
				Minecraft.getInstance().getRenderViewEntity().posY);
	}

	public static String getDisplayZ() {
		if (IEntityPlayer.isNull()) {
			return "0";
		}
		return String.format("%.3f",
				Minecraft.getInstance().getRenderViewEntity().posZ);
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
		return Minecraft.getInstance().player.dimension;
	}

	public static boolean isRowingBoat() {
		if (IEntityPlayer.isNull()) {
			return false;
		} else return Minecraft.getInstance().player.getRidingEntity() instanceof EntityBoat;
	}

	public static boolean isRiding() {
		return Minecraft.getInstance().player.isPassenger();
	}

	public static boolean isRidingHorse() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getInstance().player.isPassenger() && Minecraft.getInstance().player.getRidingEntity() instanceof AbstractHorse;
	}

	public static boolean isInLiquid() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getInstance().player.isInWater() || Minecraft.getInstance().player.isInLava();
	}

	public static boolean isFlying() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getInstance().player.abilities.isFlying;
	}

	public static void setFlying(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.abilities.isFlying = state;
	}

	public static float getFlySpeed() {
		if (IEntityPlayer.isNull()) {
			return 0F;
		}
		return Minecraft.getInstance().player.abilities.getFlySpeed();
	}

	public static void setFlySpeed(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.abilities.setFlySpeed(speed);
	}

	public static float getWalkSpeed() {
		if (IEntityPlayer.isNull()) {
			return 0F;
		}
		return Minecraft.getInstance().player.abilities.getWalkSpeed();
	}

	public static void setWalkSpeed(float speed) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.abilities.setWalkSpeed(speed);
	}

	public static String getName() {
		if (IEntityPlayer.isNull()) {
			return "";
		}
		return Minecraft.getInstance().player.getGameProfile().getName();
	}

	public static boolean isOnGround() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getInstance().player.onGround;
	}

	public static void setOnGround(boolean state) {
		if (IEntityPlayer.isNull()) {
			return;
		}
		Minecraft.getInstance().player.onGround = state;
	}

	public static boolean isOnLadder() {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		return Minecraft.getInstance().player.isOnLadder();
	}

	public static boolean isNull() {
		return Minecraft.getInstance().player == null;
	}

	public static boolean isHoldingItem(HandItem item) {
		if (IEntityPlayer.isNull()) {
			return false;
		}
		if (item.equals(HandItem.ItemBow)) {
			return Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ItemBow
					|| Minecraft.getInstance().player.getHeldItemOffhand().getItem() instanceof ItemBow;
		}
		return false;
	}

	public static boolean isSneaking() {
		return Minecraft.getInstance().player.isSneaking();
	}

	public static boolean isInAir() {
		return Minecraft.getInstance().player.areEyesInFluid(new FluidTags.Wrapper(new ResourceLocation("air")));
	}

	public static IAxisAlignedBB getBoundingBox() {
		return new IAxisAlignedBB(Minecraft.getInstance().player.getBoundingBox());
	}

	public static boolean isTouchingLiquid() {
		Minecraft mc = Minecraft.getInstance();
		boolean inLiquid = false;
		int y = (int) mc.player.getBoundingBox().minY;
		for (int x = IEntityPlayer.floor_double(mc.player.getBoundingBox().minX); x < IEntityPlayer.floor_double(mc.player.getBoundingBox().maxX) + 1; x++) {
			for (int z = IEntityPlayer.floor_double(mc.player.getBoundingBox().minZ); z < IEntityPlayer.floor_double(mc.player.getBoundingBox().maxZ)
					+ 1; z++) {
				net.minecraft.block.Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockFlowingFluid)) {
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
						NetHandlerPlayClient nethandlerplayclient = Minecraft.getInstance().player.connection;
						ping = nethandlerplayclient.getPlayerInfo(Minecraft.getInstance().player.getUniqueID()).getResponseTime();
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
		Set<?> activeParts = Minecraft.getInstance().gameSettings.getModelParts();
		for (EnumPlayerModelParts part : EnumPlayerModelParts.values()) {
			Minecraft.getInstance().gameSettings.setModelPartEnabled(part, !activeParts.contains(part));
		}
	}

	public static IBlockPos getPos() {
		return new IBlockPos(IEntityPlayer.getPosX(), IEntityPlayer.getPosY(), IEntityPlayer.getPosZ());
	}

	public static void setHorseJumpPower(float f) {
		((IMixinEntityPlayerSP) Minecraft.getInstance().player).setHorseJumpPower(f);
	}

	public static boolean isAlive() {
		return Minecraft.getInstance().player.isAlive();
	}

	public static void setAlive(boolean flag) {
		Minecraft.getInstance().player.removed = false;
		Minecraft.getInstance().player.setHealth(20f);
		Minecraft.getInstance().player.setPosition(getPosX(), getPosY(), getPosZ());
	}

	public enum HandItem {
		ItemBow
	}

}
