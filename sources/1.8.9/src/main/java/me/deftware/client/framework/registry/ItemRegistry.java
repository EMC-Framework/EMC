package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Item;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum ItemRegistry implements IRegistry.IdentifiableRegistry<Item, Void> {

	INSTANCE;

	@Override
	public Stream<Item> stream() {
		return IRegistry.streamOf(net.minecraft.item.Item.itemRegistry)
				.map(Item.class::cast);
	}

}
