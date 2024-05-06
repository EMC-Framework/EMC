package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Enchantment;

import me.deftware.client.framework.message.Message;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum EnchantmentRegistry implements IRegistry.IdentifiableRegistry<Enchantment, Void> {

	INSTANCE;

	private final RegistryWrapper.Impl<net.minecraft.enchantment.Enchantment> lookup =
			BuiltinRegistries.createWrapperLookup()
					.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

	@Override
	public Stream<Enchantment> stream() {
		return lookup.streamKeys().map(this::lookup);
	}

	private final Map<RegistryKey<net.minecraft.enchantment.Enchantment>, Enchantment> enchantmentMap = new HashMap<>();

	public synchronized Enchantment lookup(RegistryKey<net.minecraft.enchantment.Enchantment> entry) {
		return enchantmentMap.computeIfAbsent(entry, EnchantmentImpl::new);
	}

	public class EnchantmentImpl implements Enchantment {

		private final RegistryEntry<net.minecraft.enchantment.Enchantment> entry;
		private final RegistryKey<net.minecraft.enchantment.Enchantment> key;
		private final net.minecraft.enchantment.Enchantment enchantment;

		public EnchantmentImpl(RegistryKey<net.minecraft.enchantment.Enchantment> key) {
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

		// TODO: Implement

		@Override
		public int getMinLevel() {
			return 0;
		}

		@Override
		public int getMaxLevel() {
			return 0;
		}

		@Override
		public int getProtection(int level) {
			return 0;
		}

		@Override
		public float getDamage(int level) {
			return 0;
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
