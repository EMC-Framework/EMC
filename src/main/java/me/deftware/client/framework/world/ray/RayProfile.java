package me.deftware.client.framework.world.ray;

import net.minecraft.world.RayTraceContext;

public enum RayProfile {

    Block(
            RayTraceContext.ShapeType.OUTLINE,
            RayTraceContext.FluidHandling.NONE
    ),

    IncludeFluid(
            RayTraceContext.ShapeType.OUTLINE,
            RayTraceContext.FluidHandling.ANY
    );

    private final RayTraceContext.ShapeType shape;
    private final RayTraceContext.FluidHandling fluidHandling;

    RayProfile(RayTraceContext.ShapeType shape, RayTraceContext.FluidHandling fluidHandling) {
        this.shape = shape;
        this.fluidHandling = fluidHandling;
    }

    public RayTraceContext.ShapeType getShape() {
        return shape;
    }

    public RayTraceContext.FluidHandling getFluidHandling() {
        return fluidHandling;
    }

}
