package me.deftware.client.framework.registry;

import com.google.common.collect.Streams;
import me.deftware.client.framework.world.block.Block;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum BlockRegistry implements IRegistry.IdentifiableRegistry<Block, Void> {

	INSTANCE;

	@Override
	public Stream<Block> stream() {
		return Streams.stream(net.minecraft.block.Block.REGISTRY.iterator())
				.map(Block.class::cast);
	}

}
