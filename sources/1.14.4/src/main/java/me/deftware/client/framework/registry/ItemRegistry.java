package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum ItemRegistry implements IRegistry.IdentifiableRegistry<Item, Void> {

	INSTANCE;

	@Override
	public Stream<Item> stream() {
		return Registry.ITEM
				.stream()
				.map(Item.class::cast);
	}

}
