package me.deftware.client.framework.wrappers.math;

import me.deftware.client.framework.wrappers.world.IBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class IVec3d {

    public Vec3 vector, fluentVector;

    public IVec3d(double x, double y, double z) {
        vector = new Vec3(x, y, z);
        fluentVector = vector;
    }

    public IVec3d(Vec3 vector) {
        this.vector = new Vec3(vector.xCoord, vector.yCoord, vector.zCoord);
        this.fluentVector = this.vector;
    }

    public IVec3d(Vec3i vector) {
        this.vector = new Vec3(vector.getX(), vector.getY(), vector.getZ());
        this.fluentVector = this.vector;
    }

    public IVec3d(IBlockPos pos) {
        vector = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        fluentVector = vector;
    }

    public Vec3 getVector() {
        return vector;
    }

    public Vec3 getFluentVector() {
        return fluentVector;
    }

    public double getX() {
        return vector.xCoord;
    }

    public double getFluentX() {
        return fluentVector.xCoord;
    }

    public double getY() {
        return vector.yCoord;
    }

    public double getFluentY() {
        return fluentVector.yCoord;
    }

    public double getZ() {
        return vector.zCoord;
    }

    public double getFluentZ() {
        return fluentVector.zCoord;
    }

    public IVec3d scale(double scale) {
        vector = new Vec3(vector.xCoord * scale, vector.yCoord * scale, vector.zCoord * scale);
        return this;
    }

    public IVec3d rotatePitch(float p_rotatePitch_1_) {
        float lvt_2_1_ = MathHelper.cos(p_rotatePitch_1_);
        float lvt_3_1_ = MathHelper.sin(p_rotatePitch_1_);
        double lvt_4_1_ = this.getX();
        double lvt_6_1_ = this.getY() * (double)lvt_2_1_ + this.getZ() * (double)lvt_3_1_;
        double lvt_8_1_ = this.getZ() * (double)lvt_2_1_ - this.getY() * (double)lvt_3_1_;
        return new IVec3d(lvt_4_1_, lvt_6_1_, lvt_8_1_);
    }

    public IVec3d rotateYaw(float p_rotateYaw_1_) {
        float lvt_2_1_ = MathHelper.cos(p_rotateYaw_1_);
        float lvt_3_1_ = MathHelper.sin(p_rotateYaw_1_);
        double lvt_4_1_ = this.getX() * (double)lvt_2_1_ + this.getZ() * (double)lvt_3_1_;
        double lvt_6_1_ = this.getY();
        double lvt_8_1_ = this.getZ() * (double)lvt_2_1_ - this.getX() * (double)lvt_3_1_;
        return new IVec3d(lvt_4_1_, lvt_6_1_, lvt_8_1_);
    }

    public IVec3d subtract(double x, double y, double z) {
        vector = new Vec3(vector.xCoord - x, vector.yCoord - y, vector.zCoord - z);
        return this;
    }

    public IVec3d add(double x, double y, double z, boolean permanent) {
        Vec3 adjusted = new Vec3(vector.xCoord + x, vector.yCoord + y, vector.zCoord + z);

        if (permanent) {
            vector = adjusted;
        } else {
            fluentVector = adjusted;
        }

        return this;
    }

    public IVec3d add(double x, double y, double z) {
        return add(x, y, z, true);
    }

    public IVec3d add(IVec3d vector) {
        this.vector = new Vec3(this.vector.xCoord + vector.vector.xCoord, this.vector.yCoord + vector.vector.yCoord, this.vector.zCoord + vector.vector.zCoord);
        return this;
    }

	public static boolean rayTraceBlocks(IVec3d vec1, IVec3d vec2) {
        MovingObjectPosition hitResult_1 = Minecraft.getMinecraft().theWorld.rayTraceBlocks(vec1.vector, vec2.vector);
        return hitResult_1 != null && hitResult_1.typeOfHit != MovingObjectPosition.MovingObjectType.MISS;
	}

    public double squareDistanceTo(IVec3d vec) {
        return distanceTo(vec.getVector());
    }

    public double distanceTo(Vec3 p_distanceTo_1_) {
        double lvt_2_1_ = p_distanceTo_1_.xCoord - vector.xCoord;
        double lvt_4_1_ = p_distanceTo_1_.yCoord - vector.yCoord;
        double lvt_6_1_ = p_distanceTo_1_.zCoord - vector.zCoord;
        return MathHelper.sqrt_double(lvt_2_1_ * lvt_2_1_ + lvt_4_1_ * lvt_4_1_ + lvt_6_1_ * lvt_6_1_);
    }

}
