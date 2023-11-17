package me.deftware.client.framework.registry;

import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.registry.Registry;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum BlockRegistry implements IRegistry.IdentifiableRegistry<Block, Void> {

	INSTANCE;

	@Override
	public Stream<Block> stream() {
		return Registry.BLOCK
				.stream()
				.map(Block.class::cast);
	}

}
