package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Enchantment;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentType;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum EnchantmentRegistry implements IRegistry.IdentifiableRegistry<Enchantment, Void> {

	INSTANCE;

	private RegistryWrapper.Impl<net.minecraft.enchantment.Enchantment> registry;
	private final Map<RegistryKey<net.minecraft.enchantment.Enchantment>, Enchantment> enchantmentMap = new HashMap<>();

	private void sync() {
		var world = MinecraftClient.getInstance().world;
		if (world == null) {
			throw new IllegalStateException("Enchantments cannot be used when not in a world");
		}
		var registry = world.getRegistryManager();
		var enchantments = registry.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
		if (this.registry != enchantments) {
			this.registry = enchantments;
			enchantmentMap.clear();
		}
	}

	@Override
	public Stream<Enchantment> stream() {
		sync();
		return registry.streamKeys().map(key -> lookup(key, false));
	}

	public synchronized Enchantment lookup(RegistryKey<net.minecraft.enchantment.Enchantment> entry, boolean sync) {
		if (sync) {
			sync();
		}
		return enchantmentMap.computeIfAbsent(entry, key -> new EnchantmentImpl(key, registry));
	}

	private static class EnchantmentImpl implements Enchantment {

		private final RegistryEntry<net.minecraft.enchantment.Enchantment> entry;
		private final RegistryKey<net.minecraft.enchantment.Enchantment> key;
		private final net.minecraft.enchantment.Enchantment enchantment;

		public EnchantmentImpl(RegistryKey<net.minecraft.enchantment.Enchantment> key,
							   RegistryEntryLookup<net.minecraft.enchantment.Enchantment> lookup) {
			this.key = key;
			this.entry = lookup.getOrThrow(key);
			this.enchantment = entry.value();
		}

		public RegistryKey<net.minecraft.enchantment.Enchantment> getKey() {
			return key;
		}

		public RegistryEntry<net.minecraft.enchantment.Enchantment> getEntry() {
			return entry;
		}

		@Override
		public int getMinLevel() {
			return enchantment.getMinLevel();
		}

		@Override
		public int getMaxLevel() {
			return enchantment.getMaxLevel();
		}

		private float sumValue(int level, ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> type) {
			float sum = 0;
			var list = enchantment.getEffect(type);
			for (var value : list) {
				sum += value.effect().apply(level, null, 0);
			}
			return sum;
		}

		@Override
		public int getProtection(int level) {
			return (int) sumValue(level, EnchantmentEffectComponentTypes.DAMAGE_PROTECTION);
		}

		@Override
		public float getDamage(int level) {
			return sumValue(level, EnchantmentEffectComponentTypes.DAMAGE);
		}

		@Override
		public Message getName(int level) {
			return (Message) net.minecraft.enchantment.Enchantment.getName(entry, level);
		}

		@Override
		public String getTranslationKey() {
			return key.getValue().toTranslationKey();
		}

		@Override
		public String getIdentifierKey() {
			return key.getValue().getPath();
		}
	}

}
