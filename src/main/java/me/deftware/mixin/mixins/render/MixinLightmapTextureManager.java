package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.minecraft.GameSetting;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapRenderStateExtractor.class)
public class MixinLightmapTextureManager {

    @Redirect(method = "extract", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 0))
    private float getGamma(Double instance) {
        return GameSetting.GAMMA.get().floatValue();
    }

}
