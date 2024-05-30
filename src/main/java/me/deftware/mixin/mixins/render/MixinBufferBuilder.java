package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.render.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BufferBuilder.class)
public class MixinBufferBuilder {

    @ModifyVariable(method = "vertex(FFFIFFIIFFF)V", at = @At("HEAD"), index = 4, argsOnly = true)
    private int alphaModifier(int color) { // argb
        var props = Bootstrap.blockProperties;
        if (props.isActive() && props.isOpacityMode()) {
            int alpha = (int) props.getOpacity(); // 0 - 255
            return (color & 0x00ffffff) | (alpha << 24);
        }
        return color;
    }

}
