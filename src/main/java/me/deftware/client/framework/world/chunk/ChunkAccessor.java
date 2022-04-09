package me.deftware.client.framework.world.chunk;

public interface ChunkAccessor {

    SectionAccessor getSection(int index);

    int getChunkPosX();

    int getChunkPosZ();

    int getChunkHeight();

    default int getChunkMinY() {
        return 0;
    }

}
