package me.deftware.client.framework.registry;

import com.google.common.collect.Streams;
import me.deftware.client.framework.item.Enchantment;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum EnchantmentRegistry implements IRegistry.IdentifiableRegistry<Enchantment, Void> {

	INSTANCE;

	@Override
	public Stream<Enchantment> stream() {
		return Streams.stream(net.minecraft.enchantment.Enchantment.REGISTRY.iterator())
				.map(Enchantment.class::cast);
	}

}
