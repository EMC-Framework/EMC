package me.deftware.mixin.mixins.misc;

import me.deftware.client.framework.minecraft.GameSetting;
import net.minecraft.client.option.InactivityFpsLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InactivityFpsLimiter.class)
public class MixinInactivityFpsLimiter {

    @Redirect(method = "update", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/option/InactivityFpsLimiter;maxFps:I"))
    private int onGetMaxFps(InactivityFpsLimiter instance) {
        return GameSetting.MAX_FPS.get();
    }

}
