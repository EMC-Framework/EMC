package me.deftware.client.framework.registry;

import me.deftware.client.framework.world.block.Block;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum BlockRegistry implements IRegistry.IdentifiableRegistry<Block, Void> {

	INSTANCE;

	@Override
	public Stream<Block> stream() {
		return IRegistry.streamOf(net.minecraft.block.Block.blockRegistry)
				.filter(Objects::nonNull)
				.map(Block.class::cast);
	}

}
