package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EventVoxelShape extends Event {

	public VoxelShape shape;
	public boolean modified = false;
	public Block block;

	public EventVoxelShape(VoxelShape shape, Block block) {
		this.shape = shape;
		this.block = block;
	}

	public BoundingBox getBoundingBox() {
		return (BoundingBox) shape.bounds();
	}

	public void setFullCube() {
		shape = Shapes.block();
	}

	public void setEmpty() {
		shape = Shapes.empty();
	}

	public void setShape(BoundingBox bb) {
		modified = true;
		shape = Shapes.create((AABB) bb);
	}

}
