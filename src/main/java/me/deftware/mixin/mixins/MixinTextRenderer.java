package me.deftware.mixin.mixins;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class MixinTextRenderer {
    @ModifyVariable(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At("HEAD"))
    private boolean drawString(boolean shadow) {
        if (shadow) {
            return Bootstrap.EMCSettings != null && Bootstrap.EMCSettings.getPrimitive("RENDER_FONT_SHADOWS", true);
        }
        return false;
    }
}
