package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventGameOver;
import net.minecraft.client.gui.screen.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class MixinGuiGameOver {

    private boolean flag = false;

    @Inject(method = "init", at = @At("HEAD"))
    private void initGui(CallbackInfo ci) {
        if (!flag) {
            flag = true;
            new EventGameOver().broadcast();
        }
    }

}
