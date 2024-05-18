package me.deftware.mixin.mixins.render;

import lombok.Getter;
import me.deftware.client.framework.event.events.EventWeather;
import me.deftware.client.framework.render.WorldEntityRenderer;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer implements WorldEntityRenderer {

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Unique
    @Getter
    private final List<Statue> statues = new ArrayList<>();

    @Inject(method = "tickRainSplashing", at = @At("HEAD"), cancellable = true)
    private void renderRain(Camera camera, CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void renderWeather(LightmapTextureManager manager, float tickDelta, double posX, double posY, double posZ, CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();

        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"), index = 3)
    public boolean isSpectator(boolean spectator) {
        return spectator || CameraEntityMan.isActive();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;drawCurrentLayer()V"))
    private void onRenderStatues(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        MatrixStack matrices = new MatrixStack();
        for (Statue statue : this.statues) {
            float tickDelta = tickCounter.getTickDelta(true);
            this.entityRenderDispatcher.render(statue.getEntity().getMinecraftEntity(),
                    statue.getPosition().getX() - camera.getPos().getX(),
                    statue.getPosition().getY() - camera.getPos().getY(),
                    statue.getPosition().getZ() - camera.getPos().getZ(),
                    statue.getEntity().getRotationYaw(), tickDelta, matrices, this.bufferBuilders.getEntityVertexConsumers(),
                    this.entityRenderDispatcher.getLight(statue.getEntity().getMinecraftEntity(), tickDelta)
            );
        }
    }

}
