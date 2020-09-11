package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.platform.GlStateManager;
import me.deftware.client.framework.event.events.EventFogRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

    @Final
    @Shadow
    private GameRenderer gameRenderer;

    @Final
    @Shadow
    private Minecraft client;

    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private void applyFog(Camera camera, int i, CallbackInfo ci) {
        FluidState fluidState = camera.getSubmergedFluidState();
        GlStateManager.FogMode fogType;

        if (camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity)camera.getFocusedEntity()).hasStatusEffect(StatusEffects.BLINDNESS)) {
            fogType = GlStateManager.FogMode.LINEAR;
        } else if (fluidState.matches(FluidTags.WATER)) {
            fogType = GlStateManager.FogMode.EXP2;
        } else if (fluidState.matches(FluidTags.LAVA)) {
            fogType = GlStateManager.FogMode.EXP;
        } else {
            fogType = GlStateManager.FogMode.LINEAR;
        }

        EventFogRender event = new EventFogRender(camera, fogType,
                gameRenderer.getViewDistance(), client.world.dimension.shouldRenderFog(MathHelper.floor(camera.getPos().x), MathHelper.floor(camera.getPos().z)) || client.inGameHud.getBossBarHud().shouldThickenFog());
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

}