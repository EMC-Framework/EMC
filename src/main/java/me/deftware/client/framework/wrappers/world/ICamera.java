package me.deftware.client.framework.wrappers.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class ICamera {

    public static Entity getRenderViewEntity() {
        return Minecraft.getMinecraft().getRenderViewEntity();
    }

    public static double getPosX() {
        return ICamera.getRenderViewEntity().posX;
    }

    public static void setPosX(double pos) {
        ICamera.getRenderViewEntity().setPosition(pos, ICamera.getRenderViewEntity().posY, ICamera.getRenderViewEntity().posZ);
    }

    public static double getPosY() {
        return ICamera.getRenderViewEntity().posY;
    }

    public static void setPosY(double pos) {
        ICamera.getRenderViewEntity().setPosition(ICamera.getRenderViewEntity().posX, pos, ICamera.getRenderViewEntity().posZ);
    }

    public static double getPosZ() {
        return ICamera.getRenderViewEntity().posZ;
    }

    public static void setPosZ(double pos) {
        ICamera.getRenderViewEntity().setPosition(ICamera.getRenderViewEntity().posX, ICamera.getRenderViewEntity().posY, pos);
    }

    public static double getPrevPosX() {
        return ICamera.getRenderViewEntity().prevPosX;
    }

    public static double getPrevPosY() {
        return ICamera.getRenderViewEntity().prevPosY;
    }

    public static double getPrevPosZ() {
        return ICamera.getRenderViewEntity().prevPosZ;
    }

    public static double getDistance(double x, double y, double z) {
        return ICamera.getRenderViewEntity().getDistanceSq(x, y, z);
    }

}
