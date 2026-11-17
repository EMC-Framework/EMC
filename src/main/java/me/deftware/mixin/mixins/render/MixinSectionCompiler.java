package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SectionCompiler.class)
public class MixinSectionCompiler {

    @ModifyVariable(method = "getOrBeginLayer", at = @At("HEAD"), argsOnly = true)
    private ChunkSectionLayer redirectXrayOpacityLayer(ChunkSectionLayer layer) {
        var props = Bootstrap.blockProperties;
        if (props.isActive() && props.isOpacityMode() && BlockManagement.isRenderingOpacityBlock.get()) {
            return ChunkSectionLayer.TRANSLUCENT;
        }
        return layer;
    }

}
