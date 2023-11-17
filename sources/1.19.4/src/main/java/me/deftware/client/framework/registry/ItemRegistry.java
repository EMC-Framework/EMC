package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Item;
import net.minecraft.registry.Registries;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum ItemRegistry implements IRegistry.IdentifiableRegistry<Item, Void> {

	INSTANCE;

	@Override
	public Stream<Item> stream() {
		return Registries.ITEM
				.stream()
				.map(Item.class::cast);
	}

}
