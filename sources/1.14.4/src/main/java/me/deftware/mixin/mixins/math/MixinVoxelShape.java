package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.math.Voxel;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VoxelShape.class)
public class MixinVoxelShape implements Voxel {

    @Override
    public BoundingBox getBoundingBox() {
        return (BoundingBox) ((VoxelShape) (Object) this).getBoundingBox();
    }

}
