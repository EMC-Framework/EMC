package me.deftware.client.framework.utils;

import me.deftware.client.framework.wrappers.entity.IEntity;
import me.deftware.client.framework.wrappers.math.IVec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotUtils {

    private static boolean fakeRotation;
    private static float serverYaw;
    private static float serverPitch;

    public static Vec3 getCenter(AxisAlignedBB original) {
        return new Vec3(original.minX + (original.maxX - original.minX) * 0.5D, original.minY + (original.maxY - original.minY) * 0.5D, original.minZ + (original.maxZ - original.minZ) * 0.5D);
    }

    public static boolean faceEntityPacket(IEntity entity) {
        Vec3 eyesPos = RotUtils.getEyesPos();
        Vec3 lookVec = RotUtils.getServerLookVec();

        AxisAlignedBB bb = entity.getEntity().getEntityBoundingBox();
        if (RotUtils.faceVectorPacket(getCenter(bb))) {
            return true;
        }

        // TODO: Might have to reverse this?
        return bb.calculateIntercept(eyesPos, eyesPos.add(new IVec3d(lookVec).scale(6).vector)) != null;
    }

    public static boolean faceEntityClient(IEntity entity) {
        Vec3 eyesPos = RotUtils.getEyesPos();
        Vec3 lookVec = RotUtils.getServerLookVec();

        AxisAlignedBB bb = entity.getEntity().getEntityBoundingBox();
        if (RotUtils.faceVectorClient(getCenter(bb))) {
            return true;
        }

        return bb.calculateIntercept(eyesPos, eyesPos.add(new IVec3d(lookVec).scale(6).vector)) != null;
    }

    public static boolean faceVectorClient(Vec3 vec) {
        float[] rotations = RotUtils.getRotations(vec);
        float oldYaw = Minecraft.getMinecraft().thePlayer.prevRotationYaw;
        float oldPitch = Minecraft.getMinecraft().thePlayer.prevRotationPitch;
        Minecraft.getMinecraft().thePlayer.rotationYaw = RotUtils.limitAngleChange(oldYaw, rotations[0], 30);
        Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
        return Math.abs(oldYaw - rotations[0]) + Math.abs(oldPitch - rotations[1]) < 1F;
    }

    public static float getAngleToClientRotation(IEntity entity) {
        float[] needed = RotUtils.getRotations(getCenter(entity.getEntity().getEntityBoundingBox()));
        float diffYaw = MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationYaw) - needed[0];
        float diffPitch = MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch) - needed[1];
        return MathHelper.sqrt_float(diffYaw * diffYaw + diffPitch * diffPitch);
    }

    public static float getAngleToServerRotation(IEntity entity) {
        float[] needed = RotUtils.getRotations(getCenter(entity.getEntity().getEntityBoundingBox()));
        float diffYaw = RotUtils.serverYaw - needed[0];
        float diffPitch = RotUtils.serverPitch - needed[1];
        return MathHelper.sqrt_float(diffYaw * diffYaw + diffPitch * diffPitch);
    }

    private static float limitAngleChange(float current, float intended, float maxChange) {
        float change = MathHelper.wrapAngleTo180_float(intended - current);
        change = MathHelper.clamp_float(change, -maxChange, maxChange);
        return MathHelper.wrapAngleTo180_float(current + change);
    }

    private static float[] getRotations(Vec3 vec) {
        Vec3 eyesPos = RotUtils.getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - eyesPos.yCoord;
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    private static boolean faceVectorPacket(Vec3 vec) {
        RotUtils.fakeRotation = true;
        float[] rotations = RotUtils.getRotations(vec);
        RotUtils.serverYaw = RotUtils.limitAngleChange(RotUtils.serverYaw, rotations[0], 30);
        RotUtils.serverPitch = rotations[1];
        return Math.abs(RotUtils.serverYaw - rotations[0]) < 1F;
    }

    private static Vec3 getServerLookVec() {
        float f = MathHelper.cos(-RotUtils.serverYaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-RotUtils.serverYaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-RotUtils.serverPitch * 0.017453292F);
        float f3 = MathHelper.sin(-RotUtils.serverPitch * 0.017453292F);
        return new Vec3(f1 * f2, f3, f * f2);
    }

    private static Vec3 getEyesPos() {
        return new Vec3(Minecraft.getMinecraft().thePlayer.posX,
                Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(),
                Minecraft.getMinecraft().thePlayer.posZ);
    }

    public static IVec3d getEyesPosIVec() {
        return new IVec3d(Minecraft.getMinecraft().thePlayer.posX,
                Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(),
                Minecraft.getMinecraft().thePlayer.posZ);
    }

    public static float rad2deg(float rad) {
        return (float) ((180/Math.PI)*rad);
    }

    public static float deg2rad(float deg) {
        return (float) ((Math.PI/180)*deg);
    }

}
