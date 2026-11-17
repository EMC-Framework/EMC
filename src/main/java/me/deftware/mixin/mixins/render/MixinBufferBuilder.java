package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.shared.BlockManagement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BufferBuilder.class)
public class MixinBufferBuilder {

    @ModifyVariable(method = "addVertex(FFFIFFIIFFF)V", at = @At("HEAD"), index = 4, argsOnly = true)
    private int alphaModifier(int color) { // argb
        var props = Bootstrap.blockProperties;
        if (props.isActive() && props.isOpacityMode() && BlockManagement.isRenderingOpacityBlock.get()) {
            int alpha = (int) props.getOpacity(); // 0 - 255
            return (color & 0x00ffffff) | (alpha << 24);
        }
        return color;
    }

}
