package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Enchantment;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum EnchantmentRegistry implements IRegistry.IdentifiableRegistry<Enchantment, Void> {

	INSTANCE;

	@Override
	public Stream<Enchantment> stream() {
		return Stream.of(net.minecraft.enchantment.Enchantment.enchantmentsBookList)
				.filter(Objects::nonNull)
				.map(Enchantment.class::cast);
	}

}
