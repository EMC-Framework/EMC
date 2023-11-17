package me.deftware.client.framework.world.ray;

import net.minecraft.util.math.RayTraceFluidMode;

public enum RayProfile {

    Block(
            RayTraceFluidMode.NEVER
    ),

    IncludeFluid(
            RayTraceFluidMode.ALWAYS
    );

    private final RayTraceFluidMode fluidHandling;

    RayProfile(RayTraceFluidMode fluidHandling) {
        this.fluidHandling = fluidHandling;
    }

    public RayTraceFluidMode getFluidHandling() {
        return fluidHandling;
    }

}
