package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.FrameworkConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {

    @Inject(method = "render", at = @At("RETURN"))
    public void render(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (GameMap.INSTANCE.get(GameKeys.EMC_MAIN_MENU_OVERLAY, true))
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(FrameworkConstants.toDataString(), 2, 2, 0xFFFFFF);
    }

}

