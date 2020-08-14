package me.deftware.client.framework.wrappers.world;

import net.minecraft.client.Minecraft;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.chunk.Chunk;

public class IChunk {

    private final Chunk chunk;
    private final IChunkPos chunkPos;

    public IChunk(Chunk chunk) {
        this.chunk = chunk;
        this.chunkPos = new IChunkPos(chunk.getPos());
    }

    public IWorld getWorld() {
        return new IWorld(Minecraft.getInstance().world);
    }

    public int getLightFor(IEnumLightType lightType, IBlockPos pos) {
        return chunk.getLightFor(EnumLightType.valueOf(lightType.name()), pos.getPos());
    }

    public IChunkPos getPos() {
        return chunkPos;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public enum IEnumLightType {
        SKY(15),
        BLOCK(0);

        public final int defaultLightValue;

        private IEnumLightType(int defaultLightValueIn) {
            this.defaultLightValue = defaultLightValueIn;
        }

    }

}
