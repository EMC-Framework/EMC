package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.effect.StatusEffect;
import net.minecraft.potion.Potion;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum StatusEffectRegistry implements IRegistry<StatusEffect, Potion> {

	INSTANCE;

	private final HashMap<String, StatusEffect> items = new HashMap<>();
	private final HashMap<String, String> translatedNames = new HashMap<>();

	@Override
	public Stream<StatusEffect> stream() {
		return items.values().stream();
	}

	@Override
	public void register(String id, Potion object) {
		items.putIfAbsent(id, new StatusEffect(object));
		translatedNames.put(id.substring("minecraft:".length()), object.getName());
	}

	@Override
	public Optional<StatusEffect> find(String id) {
		if (translatedNames.containsKey(id)) {
			id = translatedNames.get(id);
		}
		String finalId = id;
		return stream().filter(effect ->
				effect.getUnlocalizedName().equalsIgnoreCase(finalId) ||
						effect.getUnlocalizedName().substring("potion:".length()).equalsIgnoreCase(finalId)
		).findFirst();
	}

}
