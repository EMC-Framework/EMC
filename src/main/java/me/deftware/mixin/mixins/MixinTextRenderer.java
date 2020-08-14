package me.deftware.mixin.mixins;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public abstract class MixinTextRenderer {

    private static boolean alreadyRendering = false;

    @Shadow protected abstract int renderString(String text, float x, float y, int color, boolean shadow);

    @Shadow protected abstract float renderStringAtPos(String text, float x, float y, int color, boolean shadow);

    @Inject(method = "renderString", at = @At("INVOKE"), cancellable = true)
    private void draw(String text, float x, float y, int color, boolean shadow, CallbackInfoReturnable<Integer> ci) {
        if (!alreadyRendering && shadow) {
            alreadyRendering = true;
            ci.setReturnValue(renderString(text, x, y, color, Bootstrap.EMCSettings != null && Bootstrap.EMCSettings.getPrimitive("RENDER_FONT_SHADOWS", true)));
            alreadyRendering = false;
        }
    }

    @Inject(method = "renderStringAtPos", at = @At("INVOKE"), cancellable = true)
    private void drawLayer(String text, float x, float y, int color, boolean shadow, CallbackInfoReturnable<Float> ci) {
        if (!alreadyRendering && shadow) {
            alreadyRendering = true;
            ci.setReturnValue(renderStringAtPos(text, x, y, color, Bootstrap.EMCSettings != null && Bootstrap.EMCSettings.getPrimitive("RENDER_FONT_SHADOWS", true)));
            alreadyRendering = false;
        }
    }
}
