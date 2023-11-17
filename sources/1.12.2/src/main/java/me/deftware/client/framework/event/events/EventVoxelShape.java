package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.math.AxisAlignedBB;

public class EventVoxelShape extends Event {

	public AxisAlignedBB shape;
	public boolean modified = false;
	public Block block;

	public EventVoxelShape(AxisAlignedBB shape, Block block) {
		this.shape = shape;
		this.block = block;
	}

	public BoundingBox getBoundingBox() {
		return (BoundingBox) shape;
	}

	public void setFullCube() {
		shape = net.minecraft.block.Block.FULL_BLOCK_AABB;
	}

	public void setEmpty() {
		shape = net.minecraft.block.Block.NULL_AABB;
	}

	public void setShape(BoundingBox bb) {
		modified = true;
		shape = (AxisAlignedBB) bb;
	}

}
