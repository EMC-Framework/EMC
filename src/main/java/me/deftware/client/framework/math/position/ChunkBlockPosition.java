package me.deftware.client.framework.math.position;

import me.deftware.client.framework.math.box.DoubleBoundingBox;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;

/**
 * @author Deftware
 */
public class ChunkBlockPosition extends BlockPosition {

	private final ChunkCoordIntPair chunk;

	public ChunkBlockPosition(ChunkCoordIntPair chunk) {
		super(null);
		this.chunk = chunk;
	}

	@Override
	public double getX() {
		return getMinecraftBlockPos().getX();
	}

	@Override
	public double getY() {
		return getMinecraftBlockPos().getY();
	}

	@Override
	public double getZ() {
		return getMinecraftBlockPos().getZ();
	}

	public double getStartX() {
		return chunk.getXStart();
	}

	public double getEndX() {
		return chunk.getXEnd();
	}

	public double getStartZ() {
		return chunk.getZStart();
	}

	public double getEndZ() {
		return chunk.getZEnd();
	}

	@Override
	public BlockPos getMinecraftBlockPos() {
		return new BlockPos(chunk.getXStart(), 0, chunk.getZStart());
	}

	public BlockPosition getCenterBlockPos() {
		return DoubleBlockPosition.fromMinecraftBlockPos(getMinecraftBlockPos());
	}

	public DoubleBoundingBox getBoundingBox() {
		return new DoubleBoundingBox(getStartX(), 0, getStartZ(), getEndX(), 255, getEndZ());
	}

}
