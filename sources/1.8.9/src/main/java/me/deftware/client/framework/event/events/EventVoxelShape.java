package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.math.Voxel;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.AxisAlignedBB;

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
		shape = (AxisAlignedBB) Voxel.solid();
	}

	public void setEmpty() {
		shape = (AxisAlignedBB) Voxel.empty();
	}

	public void setShape(BoundingBox bb) {
		modified = true;
		shape = (AxisAlignedBB) bb;
	}

}
