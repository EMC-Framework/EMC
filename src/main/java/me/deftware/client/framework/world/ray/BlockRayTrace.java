package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.Objects;

public class BlockRayTrace extends RayTrace<BlockSwingResult> {

    public BlockRayTrace(Vector3<Double> start, Vector3<Double> end, RayProfile profile) {
        super(start, end, profile);
    }

    @Override
    public BlockSwingResult run(Entity entity) {
        MovingObjectPosition result = Objects.requireNonNull(Minecraft.getMinecraft().theWorld).rayTraceBlocks((Vec3) start, (Vec3) end, profile.getFluidHandling());
        if (result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            return new BlockSwingResult(result);
        return null;
    }

}
