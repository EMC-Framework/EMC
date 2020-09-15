package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.box.DoubleBoundingBox;
import me.deftware.client.framework.math.box.VoxelShape;
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
		return new DoubleBoundingBox(shape);
	}

	public void setFullCube() {
		shape = VoxelShape.SOLID.getMinecraftVoxelShape();
	}

	public void setEmpty() {
		shape = VoxelShape.EMPTY.getMinecraftVoxelShape();
	}

	public void setShape(BoundingBox bb) {
		modified = true;
		shape = bb.getMinecraftBox();
	}

}
