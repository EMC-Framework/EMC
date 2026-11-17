package me.deftware.client.framework.world.ray;

import net.minecraft.world.level.ClipContext;

public enum RayProfile {

    Block(
            ClipContext.Block.OUTLINE,
            ClipContext.Fluid.NONE
    ),

    IncludeFluid(
            ClipContext.Block.OUTLINE,
            ClipContext.Fluid.ANY
    );

    private final ClipContext.Block shape;
    private final ClipContext.Fluid fluidHandling;

    RayProfile(ClipContext.Block shape, ClipContext.Fluid fluidHandling) {
        this.shape = shape;
        this.fluidHandling = fluidHandling;
    }

    public ClipContext.Block getShape() {
        return shape;
    }

    public ClipContext.Fluid getFluidHandling() {
        return fluidHandling;
    }

}
