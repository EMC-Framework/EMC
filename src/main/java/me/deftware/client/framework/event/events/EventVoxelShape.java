package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.world.IBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;

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
		shape = Block.FULL_BLOCK_AABB;
	}

	public void setEmpty() {
		shape = Block.NULL_AABB;
	}

	public void setShape(IAxisAlignedBB bb) {
		modified = true;
		shape = bb.getAABB();
	}

}
