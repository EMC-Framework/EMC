package me.deftware.mixin.mixins.render;

import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.BlockPos;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VisGraph.class)
public class MixinVisGraph {

    @Inject(method = "func_178606_a", at = @At("HEAD"), cancellable = true)
    public void setOpaqueCube(BlockPos pos, CallbackInfo ci) {
        if (Bootstrap.blockProperties.isActive())
            ci.cancel();
    }


}
