package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.main.EMCMod;
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
        if (GameMap.INSTANCE.get(GameKeys.EMC_MAIN_MENU_OVERLAY, true))
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, FrameworkConstants.toDataString(), 2, 2, 0xFFFFFF);
    }

    @Inject(at = @At("HEAD"), method = "init")
    public void oninit(CallbackInfo info) {
        if (!Bootstrap.initialized) {
            Bootstrap.initialized = true;
            Bootstrap.getMods().values().forEach(EMCMod::postInit);
        }
    }

}
