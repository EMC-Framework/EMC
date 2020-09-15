package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Item;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum ItemRegistry implements IRegistry<Item, net.minecraft.item.Item> {

	INSTANCE;

	private final HashMap<String, Item> items = new HashMap<>();
	private final HashMap<String, String> translatedNames = new HashMap<>();
	public final HashMap<String, String> namesTranslated = new HashMap<>();

	@Override
	public Stream<Item> stream() {
		return items.values().stream();
	}

	@Override
	public void register(String id, net.minecraft.item.Item object) {
		items.putIfAbsent(id, Item.newInstance(object));
		translatedNames.put(id.substring("minecraft:".length()), object.getUnlocalizedName());
		namesTranslated.put(object.getUnlocalizedName(), id.substring("minecraft:".length()));
	}

	@Override
	public Optional<Item> find(String id) {
		id = id.replace("enchanted_golden_apple", "golden_apple");
		if (translatedNames.containsKey(id)) {
			id = translatedNames.get(id);
		}
		String finalId = id;
		return stream().filter(item ->
				item.getUnlocalizedName().equalsIgnoreCase(finalId) ||
						item.getUnlocalizedName().substring("item:".length()).equalsIgnoreCase(finalId) ||
						item.getUnlocalizedName().substring("block:".length()).equalsIgnoreCase(finalId)
		).findFirst();
	}

}
