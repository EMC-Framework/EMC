package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Enchantment;
import net.minecraft.util.registry.Registry;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum EnchantmentRegistry implements IRegistry.IdentifiableRegistry<Enchantment, Void> {

	INSTANCE;

	@Override
	public Stream<Enchantment> stream() {
		return Registry.ENCHANTMENT
				.stream()
				.map(Enchantment.class::cast);
	}

}
