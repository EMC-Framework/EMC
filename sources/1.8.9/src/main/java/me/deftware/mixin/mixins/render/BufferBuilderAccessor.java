package me.deftware.mixin.mixins.render;

import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface BufferBuilderAccessor {

    @Accessor("isDrawing")
    boolean isBuilding();

}
