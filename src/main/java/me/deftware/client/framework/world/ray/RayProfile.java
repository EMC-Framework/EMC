package me.deftware.client.framework.world.ray;

public enum RayProfile {

    Block(
            false
    ),

    IncludeFluid(
            true
    );

    private final boolean fluidHandling;

    RayProfile(boolean fluidHandling) {
        this.fluidHandling = fluidHandling;
    }

    public boolean getFluidHandling() {
        return fluidHandling;
    }

}
