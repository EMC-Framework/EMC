package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.vector.Vector3d;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;

import java.util.Objects;

public class BlockRayTrace extends RayTrace<BlockSwingResult> {

    public BlockRayTrace(Vector3d start, Vector3d end, RayProfile profile) {
        super(start, end, profile);
    }

    @Override
    public BlockSwingResult run(Entity entity) {
        RayTraceResult result = Objects.requireNonNull(Minecraft.getMinecraft().world).rayTraceBlocks(start.getMinecraftVector(), end.getMinecraftVector(), profile.getFluidHandling());
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
            return new BlockSwingResult(result);
        return null;
    }

}
