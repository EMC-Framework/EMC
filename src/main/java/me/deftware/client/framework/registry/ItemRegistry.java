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
		return net.minecraft.util.registry.IRegistry.ITEM
				.stream()
				.map(Item.class::cast);
	}

}
