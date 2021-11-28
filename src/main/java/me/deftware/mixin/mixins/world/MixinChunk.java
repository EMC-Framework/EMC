package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.world.chunk.ChunkAccessor;
import me.deftware.client.framework.world.chunk.SectionAccessor;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Chunk.class)
public class MixinChunk implements ChunkAccessor {

    @Override
    public SectionAccessor getSection(int index) {
        return (SectionAccessor) ((Chunk) (Object) this).getSections()[index];
    }

    @Override
    public int getChunkPosX() {
        return ((Chunk) (Object) this).getPos().x;
    }

    @Override
    public int getChunkPosZ() {
        return ((Chunk) (Object) this).getPos().z;
    }

    @Override
    public int getChunkHeight() {
        return 256;
    }

}
