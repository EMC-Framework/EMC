package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashOverlay.class)
public class MixinSplashScreen {

    @Inject(method = "init", at = @At("HEAD"))
    private static void init(MinecraftClient client, CallbackInfo ci) {
        if (!Bootstrap.initialized) {
            Bootstrap.init();
        }
    }

}
