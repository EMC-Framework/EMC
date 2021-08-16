package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinGuiMainMenu {

    @Inject(method = "render", at = @At("RETURN"))
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (!Bootstrap.EMCSettings.hasKey("disable_main_menu_info"))
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, FrameworkConstants.toDataString(), 2, 2, 0xFFFFFF);
    }

}
