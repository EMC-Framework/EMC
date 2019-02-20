package me.deftware.client.framework.wrappers.entity;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import me.deftware.client.framework.wrappers.item.IItemStack;
import me.deftware.client.framework.wrappers.math.IVec3d;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import me.deftware.client.framework.wrappers.world.IEnumFacing;
import me.deftware.mixin.imp.IMixinEntity;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ingame.PlayerInventoryScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ScoreboardEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreboardTeam;
import net.minecraft.server.network.packet.HandSwingC2SPacket;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("All")
public class IEntityPlayer {

    private static int ping = 0;

    public static void drawPlayer(int posX, int posY, int scale) {
        PlayerInventoryScreen.drawEntity(posX, posY, scale, 0, 0, MinecraftClient.getInstance().player);
    }

    public static boolean isAtEdge() {
        return MinecraftClient.getInstance().world.getCollidingBoundingBoxesForEntity(MinecraftClient.getInstance().player, MinecraftClient.getInstance().player.getBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001), Collections.emptySet()).count() == 0;
    }

    public static boolean processRightClickBlock(IBlockPos pos, IEnumFacing facing, IVec3d vec) {
        BlockHitResult customHitResult = new BlockHitResult(vec.getVector(), IEnumFacing.getFacing(facing), pos.getPos(), false);
        return MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player,
                MinecraftClient.getInstance().world, Hand.MAIN, customHitResult) == ActionResult.SUCCESS;
    }

    public static void doJump() {
        MinecraftClient.getInstance().player.jump();
    }

    public static IItemStack getHeldItem(boolean offset) {
        ItemStack stack = MinecraftClient.getInstance().player.inventory.getMainHandStack();
        if (offset) {
            stack = MinecraftClient.getInstance().player.getOffHandStack();
        }
        if (stack == null) {
            return null;
        }
        return new IItemStack(stack);
    }

    public static float getStepHeight() {
        return MinecraftClient.getInstance().player.stepHeight;
    }

    public static void setStepHeight(float height) {
        MinecraftClient.getInstance().player.stepHeight = height;
    }

    public static IEntity getRidingEntity() {
        if (MinecraftClient.getInstance().player.getRiddenEntity() != null) {
            return new IEntity(MinecraftClient.getInstance().player.getRiddenEntity());
        }
        return null;
    }

    public static int getFoodLevel() {
        return MinecraftClient.getInstance().player.getHungerManager().getFoodLevel();
    }

    public static double getLastTickPosX() {
        return MinecraftClient.getInstance().player.prevRenderX;
    }

    public static double getLastTickPosY() {
        return MinecraftClient.getInstance().player.prevRenderY;
    }

    public static double getLastTickPosZ() {
        return MinecraftClient.getInstance().player.prevRenderZ;
    }

    public static IEntity clonePlayer() {
        return new IEntity(new IEntityOtherPlayerMP());
    }

    public static boolean isAirBorne() {
        return MinecraftClient.getInstance().player.velocityDirty;
    }

    public static boolean getFlag(int flag) {
        return ((IMixinEntity) MinecraftClient.getInstance().player).getAFlag(flag);
    }

    public static void setSprinting(boolean state) {
        MinecraftClient.getInstance().player.setSprinting(state);
    }

    public static float getMoveStrafing() {
        return MinecraftClient.getInstance().player.field_6212;
    }

    public static float getMoveForward() {
        return MinecraftClient.getInstance().player.field_6250;
    }

    public static boolean isCollidedHorizontally() {
        return MinecraftClient.getInstance().player.horizontalCollision;
    }

    public static boolean isRidingEntityInWater() {
        return MinecraftClient.getInstance().player.getRiddenEntity().isInsideWater();
    }

    public static double getRidingEntityMotionX() {
        return MinecraftClient.getInstance().player.getRiddenEntity().velocityX;
    }

    public static double getRidingEntityMotionY() {
        return MinecraftClient.getInstance().player.getRiddenEntity().velocityY;
    }

    public static double getRidingEntityMotionZ() {
        return MinecraftClient.getInstance().player.getRiddenEntity().velocityZ;
    }

    public static void ridingEntityMotionY(double y) {
        MinecraftClient.getInstance().player.getRiddenEntity().velocityY = y;
    }

    public static void ridingEntityMotionX(double x) {
        MinecraftClient.getInstance().player.getRiddenEntity().velocityX = x;
    }

    public static void ridingEntityMotionZ(double z) {
        MinecraftClient.getInstance().player.getRiddenEntity().velocityZ = z;
    }

    public static void ridingEntityMotionTimesY(double y) {
        MinecraftClient.getInstance().player.getRiddenEntity().velocityY *= y;
    }

    public static void ridingEntityMotionTimesX(double x) {
        MinecraftClient.getInstance().player.getRiddenEntity().velocityX *= x;
    }

    public static boolean isRidingOnGround() {
        return MinecraftClient.getInstance().player.getRiddenEntity().onGround;
    }

    public static void ridingEntityMotionTimesZ(double z) {
        MinecraftClient.getInstance().player.getRiddenEntity().velocityZ *= z;
    }

    public static void attackEntity(IEntity entity) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().interactionManager.attackEntity(MinecraftClient.getInstance().player, entity.getEntity());
        IEntityPlayer.swingArmClientSide();
    }

    public static boolean isCreative() {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        return MinecraftClient.getInstance().player.isCreative();
    }

    public static void setPositionY(int y) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.setPosition(MinecraftClient.getInstance().player.x,
                MinecraftClient.getInstance().player.y + y, MinecraftClient.getInstance().player.z);
    }

    public static void setPosition(double x, double y, double z) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.setPosition(x, y, z);
    }

    public static void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.setPositionAndAngles(x, y, z, yaw, pitch);
    }

    public static void setJumpMovementFactor(float speed) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.field_6281 = speed;
    }

    public static void setJumpMovementFactorTimes(float speed) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.field_6281 *= speed;
    }

    public static void setNoClip(boolean state) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.noClip = state;
    }

    public static void setFalldistance(float distance) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.fallDistance = distance;
    }

    public static double getMotionX() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.velocityX;
    }

    public static void setMotionX(double x) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityX = x;
    }

    public static double getMotionY() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.velocityY;
    }

    public static void setMotionY(double y) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityY = y;
    }

    public static double getMotionZ() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.velocityZ;
    }

    public static void setMotionZ(double z) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityZ = z;
    }

    public static void setMotionTimesX(double x) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityX *= x;
    }

    public static void setMotionTimesY(double y) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityY *= y;
    }

    public static void setMotionTimesZ(double z) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityZ *= z;
    }

    public static void setMotionPlusX(double x) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityX += x;
    }

    public static void setMotionPlusY(double y) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityY += y;
    }

    public static void setMotionPlusZ(double z) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityZ += z;
    }

    public static void setMotionMinusX(double x) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityX -= x;
    }

    public static void setMotionMinusY(double y) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityY -= y;
    }

    public static void setMotionMinusZ(double z) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.velocityZ -= z;
    }

    public static void respawnPlayer() {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.requestRespawn();
    }

    public static void swingArmClientSide() {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.swingHand(Hand.MAIN);
    }

    public static float getSaturationLevel() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.getHungerManager().getSaturationLevel();
    }

    public static void swingArmPacket() {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN));
    }

    public static float getCooldown() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.method_7261(0);
    }

    public static float getRotationYaw() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.yaw;
    }

    public static void setRotationYaw(float yaw) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.yaw = yaw;
    }

    public static float getRotationPitch() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.pitch;
    }

    public static void setRotationPitch(float pitch) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.pitch = pitch;
    }

    public static double getPosX() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.x;
    }

    public static double getPosY() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.y;
    }

    public static double getPosZ() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.z;
    }

    public static double getEyeHeight() {
        return MinecraftClient.getInstance().player.getEyeHeight();
    }

    public static int getItemInUseMaxCount() {
        return MinecraftClient.getInstance().player.method_6014();
    }

    public static double getPrevPosX() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.prevX;
    }

    public static double getPrevPosY() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.prevY;
    }

    public static double getPrevPosZ() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.prevZ;
    }

    public static float getHealth() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.getHealth();
    }

    public static float getFallDistance() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.fallDistance;
    }

    public static boolean hasPotionEffects() {
        if (!MinecraftClient.getInstance().player.getPotionEffects().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isSingleplayer() {
        if (IEntityPlayer.isNull()) {
            return true;
        }
        return MinecraftClient.getInstance().isInSingleplayer();
    }

    public static String getDisplayX() {
        if (IEntityPlayer.isNull()) {
            return "0";
        }
        return String.format("%.3f",
                new Object[]{Double.valueOf(MinecraftClient.getInstance().getCameraEntity().x)});
    }

    public static String getDisplayY() {
        if (IEntityPlayer.isNull()) {
            return "0";
        }
        return String.format("%.5f",
                new Object[]{Double.valueOf(MinecraftClient.getInstance().getCameraEntity().y)});
    }

    public static String getDisplayZ() {
        if (IEntityPlayer.isNull()) {
            return "0";
        }
        return String.format("%.3f",
                new Object[]{Double.valueOf(MinecraftClient.getInstance().getCameraEntity().z)});
    }

    public static int getPing() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        Ordering<ScoreboardEntry> ENTRY_ORDERING = Ordering.from(new PlayerComparator());
        new Thread() {
            @Override
            public void run() {
                ClientPlayNetworkHandler nethandlerplayclient = MinecraftClient.getInstance().player.networkHandler;
                List<ScoreboardEntry> list = ENTRY_ORDERING
                        .<ScoreboardEntry>sortedCopy(nethandlerplayclient.getScoreboardEntries());

                for (ScoreboardEntry networkplayerinfo : list) {
                    String uuid = networkplayerinfo.getProfile().getId().toString();
                    if (uuid.equals(MinecraftClient.getInstance().player.getUuid().toString())) {
                        IEntityPlayer.ping = networkplayerinfo.getLatency();
                    }
                }
            }
        }.start();
        return IEntityPlayer.ping;
    }

    /**
     * Which dimension the player is in (-1 = the Nether, 0 = normal world)
     *
     * @return
     */
    public static int getDimension() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return MinecraftClient.getInstance().player.dimension.getRawId();
    }

    public static boolean isRowingBoat() {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        return MinecraftClient.getInstance().player.method_3144();
    }

    public static boolean isRiding() {
        return MinecraftClient.getInstance().player.hasVehicle();
    }

    public static boolean isRidingHorse() {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        // TODO: Does this work?
        return MinecraftClient.getInstance().player.hasVehicle() && MinecraftClient.getInstance().player.getRiddenEntity() instanceof HorseEntity;
    }

    public static boolean isInLiquid() {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        return MinecraftClient.getInstance().player.isInsideWater() || MinecraftClient.getInstance().player.isTouchingLava();
    }

    public static boolean isFlying() {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        return MinecraftClient.getInstance().player.abilities.flying;
    }

    public static void setFlying(boolean state) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.abilities.flying = state;
    }

    public static float getFlySpeed() {
        if (IEntityPlayer.isNull()) {
            return 0F;
        }
        return MinecraftClient.getInstance().player.abilities.getFlySpeed();
    }

    public static void setFlySpeed(float speed) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.abilities.setFlySpeed(speed);
    }

    public static float getWalkSpeed() {
        if (IEntityPlayer.isNull()) {
            return 0F;
        }
        return MinecraftClient.getInstance().player.abilities.getWalkSpeed();
    }

    public static void setWalkSpeed(float speed) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.abilities.setWalkSpeed(speed);
    }

    public static String getName() {
        if (IEntityPlayer.isNull()) {
            return "";
        }
        return MinecraftClient.getInstance().player.getGameProfile().getName();
    }

    public static boolean isOnGround() {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        return MinecraftClient.getInstance().player.onGround;
    }

    public static void setOnGround(boolean state) {
        if (IEntityPlayer.isNull()) {
            return;
        }
        MinecraftClient.getInstance().player.onGround = state;
    }

    public static boolean isOnLadder() {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        // TODO: Is this right?
        return MinecraftClient.getInstance().player.canClimb();
    }

    public static boolean isNull() {
        if (MinecraftClient.getInstance().player == null) {
            return true;
        }
        return false;
    }

    public static boolean isHoldingItem(HandItem item) {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        if (item.equals(HandItem.ItemBow)) {
            return MinecraftClient.getInstance().player.getMainHandStack().getItem() instanceof BowItem
                    || MinecraftClient.getInstance().player.getOffHandStack().getItem() instanceof BowItem;
        }
        return false;
    }

    public static boolean isSneaking() {
        return MinecraftClient.getInstance().player.isSneaking();
    }

    public static boolean isInAir() {
        return MinecraftClient.getInstance().player.isInFluid(new FluidTags.class_3487(new Identifier("air")));
    }

    public static boolean isTouchingLiquid() {
        MinecraftClient mc = MinecraftClient.getInstance();
        boolean inLiquid = false;
        int y = (int) mc.player.getBoundingBox().minY;
        for (int x = IEntityPlayer.floor_double(mc.player.getBoundingBox().minX); x < IEntityPlayer.floor_double(mc.player.getBoundingBox().maxX) + 1; x++) {
            for (int z = IEntityPlayer.floor_double(mc.player.getBoundingBox().minZ); z < IEntityPlayer.floor_double(mc.player.getBoundingBox().maxZ)
                    + 1; z++) {
                net.minecraft.block.Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if ((block != null) && (!(block instanceof AirBlock))) {
                    if (!(block instanceof FluidBlock)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }

    public static int floor_double(double value) {
        int i = (int) value;
        return value < i ? i - 1 : i;
    }

    public static void toggleSkinLayers() {
        Set<?> activeParts = MinecraftClient.getInstance().options.getEnabledPlayerModelParts();
        for (PlayerModelPart part : PlayerModelPart.values()) {
            MinecraftClient.getInstance().options.setPlayerModelPart(part, !activeParts.contains(part));
        }
    }

    public static IBlockPos getPos() {
        return new IBlockPos(IEntityPlayer.getPosX(), IEntityPlayer.getPosY(), IEntityPlayer.getPosZ());
    }

    public static void setHorseJumpPower(float f) {
        ((IMixinEntityPlayerSP) MinecraftClient.getInstance().player).setHorseJumpPower(f);
    }

    public enum HandItem {
        ItemBow
    }

    static class PlayerComparator implements Comparator<ScoreboardEntry> {

        @Override
        public int compare(ScoreboardEntry p_compare_1_, ScoreboardEntry p_compare_2_) {
            ScoreboardTeam scoreplayerteam = p_compare_1_.getScoreboardTeam();
            ScoreboardTeam scoreplayerteam1 = p_compare_2_.getScoreboardTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameMode() != GameMode.SPECTATOR, p_compare_2_.getGameMode() != GameMode.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getName() : "").compare(p_compare_1_.getProfile().getName(), p_compare_2_.getProfile().getName()).result();
        }

    }

}
