package me.deftware.mixin.mixins;

import me.deftware.mixin.imp.IMixinRenderManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class MixinRenderManager implements IMixinRenderManager {

    public double renderPosX = 0;

    public double renderPosY = 0;

    public double renderPosZ = 0;

    public double getRenderPosX() {
        return renderPosX;
    }

    public double getRenderPosY() {
        return renderPosY;
    }

    public double getRenderPosZ() {
        return renderPosZ;
    }

    @Inject(method = "configure", at = @At("HEAD"))
    public void configure(World world_1, Camera camera_1, Entity entity_1, CallbackInfo ci) {
        renderPosX = camera_1.getPos().x;
        renderPosY = camera_1.getPos().y;
        renderPosZ = camera_1.getPos().z;
    }

}
