package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.gui.SplashScreen;
import net.minecraft.util.SystemUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashScreen.class)
public class MixinSplashScreen {

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        if (!Bootstrap.initialized) {
            Bootstrap.init();
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/SystemUtil;getMeasuringTimeMs()J", ordinal = 1))
    private long render(int mouseX, int mouseY, float tickDelta) {
        if (!Bootstrap.initialized) {
            Bootstrap.initialized = true;
            Bootstrap.getMods().values().forEach(EMCMod::postInit);
        }
        return SystemUtil.getMeasuringTimeMs();
    }

}
