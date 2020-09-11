package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.mixin.imp.IMixinTitleScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MainMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MainMenuScreen.class)
public abstract class MixinGuiMainMenu implements IMixinTitleScreen {

    @Shadow
    protected abstract void switchToRealms();

    @Inject(method = "render", at = @At("RETURN"))
    public void render(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "MAIN_MENU_OVERLAY", true)) {
            net.minecraft.client.Minecraft.getInstance().textRenderer.drawWithShadow(FrameworkConstants.toDataString(), 2, 2, 0xFFFFFF);
        }
    }

    @Override
    public void switchToRealmsPub() {
        switchToRealms();
    }
}
