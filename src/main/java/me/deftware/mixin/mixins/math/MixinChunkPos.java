package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.ChunkPosition;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChunkPos.class)
public class MixinChunkPos implements ChunkPosition {

    @Unique
    @Override
    public int getStartX() {
        return ((ChunkPos) (Object) this).getStartX();
    }

    @Unique
    @Override
    public int getStartZ() {
        return ((ChunkPos) (Object) this).getStartZ();
    }

    @Unique
    @Override
    public int getEndX() {
        return ((ChunkPos) (Object) this).getEndX();
    }

    @Unique
    @Override
    public int getEndZ() {
        return ((ChunkPos) (Object) this).getEndZ();
    }

    @Unique
    @Override
    public BlockPosition getCenter() {
        return BlockPosition.of(
                getStartX() + 8, 0, getStartZ() + 8
        );
    }

}
