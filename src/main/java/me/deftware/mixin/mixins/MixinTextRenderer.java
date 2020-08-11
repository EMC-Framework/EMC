package me.deftware.mixin.mixins;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextRenderer.class)
public abstract class MixinTextRenderer {

    private static boolean alreadyRendering = false;

    @Shadow protected abstract int draw(String text, float x, float y, int color, boolean shadow);

    @Shadow protected abstract float drawLayer(String text, float x, float y, int color, boolean shadow);

    @Inject(method = "draw(Ljava/lang/String;FFIZ)I", at = @At("INVOKE"), cancellable = true)
    private void draw(String text, float x, float y, int color, boolean shadow, CallbackInfoReturnable<Integer> ci) {
        if (!alreadyRendering && shadow) {
            alreadyRendering = true;
            ci.setReturnValue(draw(text, x, y, color, Bootstrap.EMCSettings != null && Bootstrap.EMCSettings.getPrimitive("RENDER_FONT_SHADOWS", true)));
            alreadyRendering = false;
        }
    }

    @Inject(method = "drawLayer", at = @At("INVOKE"), cancellable = true)
    private void drawLayer(String text, float x, float y, int color, boolean shadow, CallbackInfoReturnable<Float> ci) {
        if (!alreadyRendering && shadow) {
            alreadyRendering = true;
            ci.setReturnValue(drawLayer(text, x, y, color, Bootstrap.EMCSettings != null && Bootstrap.EMCSettings.getPrimitive("RENDER_FONT_SHADOWS", true)));
            alreadyRendering = false;
        }
    }
}
