package me.deftware.client.framework.registry;

import me.deftware.client.framework.world.block.Block;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum BlockRegistry implements IRegistry.IdentifiableRegistry<Block, Void> {

	INSTANCE;

	@Override
	public Stream<Block> stream() {
		return net.minecraft.util.registry.IRegistry.BLOCK
				.stream()
				.map(Block.class::cast);
	}

}
