package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.ChunkPosition;
import net.minecraft.world.ChunkCoordIntPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChunkCoordIntPair.class)
public class MixinChunkPos implements ChunkPosition {

    @Unique
    @Override
    public int getStartX() {
        return ((ChunkCoordIntPair) (Object) this).getXStart();
    }

    @Unique
    @Override
    public int getStartZ() {
        return ((ChunkCoordIntPair) (Object) this).getZStart();
    }

    @Unique
    @Override
    public int getEndX() {
        return ((ChunkCoordIntPair) (Object) this).getXEnd();
    }

    @Unique
    @Override
    public int getEndZ() {
        return ((ChunkCoordIntPair) (Object) this).getZEnd();
    }

    @Unique
    @Override
    public BlockPosition getCenter() {
        return BlockPosition.of(
                getStartX() + 8, 0, getStartZ() + 8
        );
    }

}
