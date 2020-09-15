package me.deftware.client.framework.world.chunk;

import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.ChunkBlockPosition;
import net.minecraft.world.EnumSkyBlock;

/**
 * @author Deftware
 */
public class Chunk {

	protected final net.minecraft.world.chunk.Chunk chunk;
	protected final ChunkBlockPosition position;

	public Chunk(net.minecraft.world.chunk.Chunk chunk) {
		this.chunk = chunk;
		this.position = new ChunkBlockPosition(chunk.getChunkCoordIntPair());
	}

	public ChunkBlockPosition getChunkPosition() {
		return position;
	}

	public net.minecraft.world.chunk.Chunk getMinecraftChunk() {
		return chunk;
	}

	public int getLuminanceAt(BlockPosition pos) {
		return chunk.getLightFor(EnumSkyBlock.BLOCK, pos.getMinecraftBlockPos());
	}
	
}
