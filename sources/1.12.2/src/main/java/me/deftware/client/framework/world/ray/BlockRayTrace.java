package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class BlockRayTrace extends RayTrace<BlockSwingResult> {

    public BlockRayTrace(Vector3<Double> start, Vector3<Double> end, RayProfile profile) {
        super(start, end, profile);
    }

    @Override
    public BlockSwingResult run(Entity entity) {
        RayTraceResult result = Objects.requireNonNull(Minecraft.getMinecraft().world).rayTraceBlocks((Vec3d) start, (Vec3d) end, profile.getFluidHandling());
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
            return new BlockSwingResult(result);
        return null;
    }

}
