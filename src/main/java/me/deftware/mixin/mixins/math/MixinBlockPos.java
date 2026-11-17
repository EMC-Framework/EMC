package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.world.EnumFacing;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockPos.class)
public class MixinBlockPos extends MixinVector3i implements BlockPosition {

    @Unique
    @Override
    public BlockPosition offset(EnumFacing direction) {
        return (BlockPosition) ((BlockPos) (Object) this).relative(direction.getFacing(), 1);
    }

    @Unique
    @Override
    public long asLong() {
        return ((BlockPos) (Object) this).asLong();
    }

}
