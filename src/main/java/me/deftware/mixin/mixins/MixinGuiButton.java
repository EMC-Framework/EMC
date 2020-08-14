package me.deftware.mixin.mixins;

import me.deftware.mixin.imp.IMixinGuiButton;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiButton.class)
public class MixinGuiButton implements IMixinGuiButton {

    @Shadow
    private boolean hovered;

    @Override
    public void setIsHovered(boolean state) {
        hovered = state;
    }

}
