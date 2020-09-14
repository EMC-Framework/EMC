package me.deftware.client.framework.world.classifier;

import me.deftware.client.framework.math.box.BoundingBox;
import net.minecraft.block.Block;

/**
 * @author Deftware
 */
public class ClassifiedBlock {
	private final BoundingBox box;
	private final Block block;

	public BoundingBox getBox() {
		return this.box;
	}

	public Block getBlock() {
		return this.block;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof ClassifiedBlock)) return false;
		final ClassifiedBlock other = (ClassifiedBlock) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$box = this.getBox();
		final Object other$box = other.getBox();
		if (this$box == null ? other$box != null : !this$box.equals(other$box)) return false;
		final Object this$block = this.getBlock();
		final Object other$block = other.getBlock();
		if (this$block == null ? other$block != null : !this$block.equals(other$block)) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof ClassifiedBlock;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $box = this.getBox();
		result = result * PRIME + ($box == null ? 43 : $box.hashCode());
		final Object $block = this.getBlock();
		result = result * PRIME + ($block == null ? 43 : $block.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ClassifiedBlock(box=" + this.getBox() + ", block=" + this.getBlock() + ")";
	}

	public ClassifiedBlock(final BoundingBox box, final Block block) {
		this.box = box;
		this.block = block;
	}
}
