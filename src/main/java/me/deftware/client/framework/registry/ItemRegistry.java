package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum ItemRegistry implements IRegistry<Item, net.minecraft.item.Item> {

	INSTANCE;

	private final HashMap<String, Item> items = new HashMap<>();

	private final Map<net.minecraft.item.Item, Item> map = new HashMap<>();

	@Override
	public Stream<Item> stream() {
		return items.values().stream();
	}

	@Override
	public void register(String id, net.minecraft.item.Item object) {
		Item item = Item.newInstance(object);
		items.putIfAbsent(id, item);
		map.putIfAbsent(object, item);
	}

	@Override
	public Optional<Item> find(String id) {
		String finalId;
		if (id.equalsIgnoreCase("enchanted_golden_apple")) finalId = "golden_apple"; else finalId = id;
		return stream().filter(item ->
				item.getTranslationKey().equalsIgnoreCase(finalId) ||
						item.getTranslationKey().substring("minecraft:".length()).equalsIgnoreCase(finalId)
		).findFirst();
	}

	public Item getItem(net.minecraft.item.Item item) {
		return map.get(item);
	}

}
