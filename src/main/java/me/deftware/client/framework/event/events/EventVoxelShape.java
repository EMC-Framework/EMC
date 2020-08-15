package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.world.IBlock;
import net.minecraft.util.AxisAlignedBB;

public class EventVoxelShape extends Event {

	public AxisAlignedBB shape;
	public boolean modified = false;
	public IBlock block;

	public EventVoxelShape(AxisAlignedBB shape, IBlock block) {
		this.shape = shape;
		this.block = block;
	}

	public IAxisAlignedBB getBoundingBox() {
		return new IAxisAlignedBB(shape);
	}

	public void setFullCube() {
		shape = IAxisAlignedBB.FULL_BLOCK_AABB;
	}

	public void setEmpty() {
		shape = IAxisAlignedBB.NULL_AABB;
	}

	public void setShape(IAxisAlignedBB bb) {
		modified = true;
		shape = bb.getAABB();
	}

}
