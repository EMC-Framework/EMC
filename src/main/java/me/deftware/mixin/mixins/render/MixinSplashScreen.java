package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.gui.GuiScreenLoading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenLoading.class)
public class MixinSplashScreen {

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        if (!Bootstrap.initialized) {
            Bootstrap.init();
        }
    }

}
