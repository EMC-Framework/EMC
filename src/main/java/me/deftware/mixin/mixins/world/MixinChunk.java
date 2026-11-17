package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.world.chunk.ChunkAccessor;
import me.deftware.client.framework.world.chunk.SectionAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelChunk.class)
public class MixinChunk implements ChunkAccessor {

    @Override
    public SectionAccessor getSection(int index) {
        return (SectionAccessor) ((LevelChunk) (Object) this).getSection(index);
    }

    @Override
    public int getChunkPosX() {
        return ((LevelChunk) (Object) this).getPos().x();
    }

    @Override
    public int getChunkPosZ() {
        return ((LevelChunk) (Object) this).getPos().z();
    }

    @Override
    public int getChunkHeight() {
        return ((LevelChunk) (Object) this).getHeight();
    }

    @Override
    public int getChunkMinY() {
        return ((LevelChunk) (Object) this).getMinY();
    }

}
