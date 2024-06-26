package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Objects;

public class BlockRayTrace extends RayTrace<BlockSwingResult> {

    public BlockRayTrace(Vector3<Double> start, Vector3<Double> end, RayProfile profile) {
        super(start, end, profile);
    }

    @Override
    public BlockSwingResult run(Entity entity) {
        BlockHitResult result = Objects.requireNonNull(MinecraftClient.getInstance().world).raycast(getContext(entity));
        if (result != null && result.getType() == HitResult.Type.BLOCK)
            return new BlockSwingResult(result);
        return null;
    }

}
