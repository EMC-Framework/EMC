package me.deftware.mixin.mixins;

import me.deftware.mixin.imp.IMixinRenderManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderManager.class)
public class MixinRenderManager implements IMixinRenderManager {

    @Shadow
    private double renderPosX = 0;

    @Shadow
    private double renderPosY = 0;

    @Shadow
    private double renderPosZ = 0;

    public double getRenderPosX() {
        return renderPosX;
    }

    public double getRenderPosY() {
        return renderPosY;
    }

    public double getRenderPosZ() {
        return renderPosZ;
    }

}
