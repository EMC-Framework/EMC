package me.deftware.client.framework.registry;

import com.google.common.collect.Streams;
import me.deftware.client.framework.item.Item;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum ItemRegistry implements IRegistry.IdentifiableRegistry<Item, Void> {

	INSTANCE;

	@Override
	public Stream<Item> stream() {
		return Streams.stream(net.minecraft.item.Item.REGISTRY.iterator())
				.map(Item.class::cast);
	}

}
