package me.deftware.client.framework.registry;

import me.deftware.client.framework.world.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum BlockRegistry implements IRegistry.IdentifiableRegistry<Block, Void> {

	INSTANCE;

	@Override
	public Stream<Block> stream() {
		return BuiltInRegistries.BLOCK
				.stream()
				.map(Block.class::cast);
	}

}
