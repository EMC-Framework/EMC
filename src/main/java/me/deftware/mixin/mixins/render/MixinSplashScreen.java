package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class MixinSplashScreen {

    @Shadow
    @Final
    private boolean fadeIn;

    @Inject(method = "registerTextures", at = @At("HEAD"))
    private static void init(TextureManager textureManager, CallbackInfo ci) {
        Bootstrap.init();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init(II)V"))
    private void onInit(Screen instance, int width, int height) {
        if (!fadeIn) {
            Bootstrap.getMods().values().forEach(EMCMod::postInit);
        }
        instance.init(width, height);
    }

}
