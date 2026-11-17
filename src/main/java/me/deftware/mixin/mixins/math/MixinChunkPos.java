package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.ChunkPosition;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChunkPos.class)
public class MixinChunkPos implements ChunkPosition {

    @Unique
    @Override
    public int getStartX() {
        return ((ChunkPos) (Object) this).getMinBlockX();
    }

    @Unique
    @Override
    public int getStartZ() {
        return ((ChunkPos) (Object) this).getMinBlockZ();
    }

    @Unique
    @Override
    public int getEndX() {
        return ((ChunkPos) (Object) this).getMaxBlockX();
    }

    @Unique
    @Override
    public int getEndZ() {
        return ((ChunkPos) (Object) this).getMaxBlockZ();
    }

    @Unique
    @Override
    public BlockPosition getCenter() {
        return BlockPosition.of(
                ((ChunkPos) (Object) this).getMiddleBlockX(), 0, ((ChunkPos) (Object) this).getMiddleBlockZ()
        );
    }

}
