package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.enchantment.Enchantment;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum EnchantmentRegistry implements IRegistry<Enchantment, net.minecraft.enchantment.Enchantment> {

	INSTANCE;

	private final HashMap<String, Enchantment> enchantments = new HashMap<>();
	private final HashMap<String, String> translatedNames = new HashMap<>();

	@Override
	public Stream<Enchantment> stream() {
		return enchantments.values().stream();
	}

	@Override
	public void register(String id, net.minecraft.enchantment.Enchantment object) {
		try {
			enchantments.putIfAbsent(id, new Enchantment(object));
			translatedNames.put(id.substring("minecraft:".length()), object.getName());
		} catch (Throwable ignored) { }
	}

	@Override
	public Optional<Enchantment> find(String id) {
		if (translatedNames.containsKey(id)) {
			id = translatedNames.get(id);
		}
		String finalId = id;
		return stream().filter(enchantment ->
				enchantment.getUnlocalizedName().equalsIgnoreCase(finalId) ||
						enchantment.getUnlocalizedName().substring("enchantment:".length()).equalsIgnoreCase(finalId)
		).findFirst();
	}

}
