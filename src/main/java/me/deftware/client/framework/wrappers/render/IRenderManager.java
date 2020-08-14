package me.deftware.client.framework.wrappers.render;

import me.deftware.client.framework.utils.render.RenderUtils;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.Minecraft;

public class IRenderManager {

    public static double getRenderPosX() {
        return (RenderUtils.getRenderManager()).getRenderPosX();
    }

    public static double getRenderPosY() {
        return (RenderUtils.getRenderManager()).getRenderPosY();
    }

    public static double getRenderPosZ() {
        return (RenderUtils.getRenderManager()).getRenderPosZ();
    }

    public static float getPlayerViewY() {
        return Minecraft.getInstance().getRenderManager().playerViewY;
    }

    public static float getPlayerViewX() {
        return Minecraft.getInstance().getRenderManager().playerViewX;
    }

    public static float getPlayerFovMultiplier() {
        return ((IMixinEntityRenderer) Minecraft.getInstance().gameRenderer).getFovMultiplier();
    }

    public static void updatePlayerFovMultiplier(float newValue) {
        ((IMixinEntityRenderer) Minecraft.getInstance().gameRenderer).updateFovMultiplier(newValue);
    }

}
