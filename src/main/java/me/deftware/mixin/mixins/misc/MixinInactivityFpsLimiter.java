package me.deftware.mixin.mixins.misc;

import com.mojang.blaze3d.platform.FramerateLimitTracker;
import me.deftware.client.framework.minecraft.GameSetting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FramerateLimitTracker.class)
public class MixinInactivityFpsLimiter {

    @Redirect(method = "getFramerateLimit", at = @At(value = "FIELD",
            target = "Lcom/mojang/blaze3d/platform/FramerateLimitTracker;framerateLimit:I"))
    private int onGetMaxFps(FramerateLimitTracker instance) {
        return GameSetting.MAX_FPS.get();
    }

}
