package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Enchantment;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum EnchantmentRegistry implements IRegistry.IdentifiableRegistry<Enchantment, Void> {

	INSTANCE;

	private HolderLookup.RegistryLookup<net.minecraft.world.item.enchantment.Enchantment> registry;
	private final Map<ResourceKey<net.minecraft.world.item.enchantment.Enchantment>, Enchantment> enchantmentMap = new HashMap<>();

	private void sync() {
		var world = Minecraft.getInstance().level;
		if (world == null) {
			throw new IllegalStateException("Enchantments cannot be used when not in a world");
		}
		var registry = world.registryAccess();
		var enchantments = registry.lookupOrThrow(Registries.ENCHANTMENT);
		if (this.registry != enchantments) {
			this.registry = enchantments;
			enchantmentMap.clear();
		}
	}

	@Override
	public Stream<Enchantment> stream() {
		sync();
		return registry.listElementIds().map(key -> lookup(key, false));
	}

	public synchronized Enchantment lookup(ResourceKey<net.minecraft.world.item.enchantment.Enchantment> entry, boolean sync) {
		if (sync) {
			sync();
		}
		return enchantmentMap.computeIfAbsent(entry, key -> new EnchantmentImpl(key, registry));
	}

	private static class EnchantmentImpl implements Enchantment {

		private final Holder<net.minecraft.world.item.enchantment.Enchantment> entry;
		private final ResourceKey<net.minecraft.world.item.enchantment.Enchantment> key;
		private final net.minecraft.world.item.enchantment.Enchantment enchantment;

		public EnchantmentImpl(ResourceKey<net.minecraft.world.item.enchantment.Enchantment> key,
							   HolderGetter<net.minecraft.world.item.enchantment.Enchantment> lookup) {
			this.key = key;
			this.entry = lookup.getOrThrow(key);
			this.enchantment = entry.value();
		}

		public ResourceKey<net.minecraft.world.item.enchantment.Enchantment> getKey() {
			return key;
		}

		public Holder<net.minecraft.world.item.enchantment.Enchantment> getEntry() {
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

		private float sumValue(int level, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> type) {
			float sum = 0;
			var list = enchantment.getEffects(type);
			for (var value : list) {
				sum += value.effect().process(level, null, 0);
			}
			return sum;
		}

		@Override
		public int getProtection(int level) {
			return (int) sumValue(level, EnchantmentEffectComponents.DAMAGE_PROTECTION);
		}

		@Override
		public float getDamage(int level) {
			return sumValue(level, EnchantmentEffectComponents.DAMAGE);
		}

		@Override
		public Message getName(int level) {
			return (Message) net.minecraft.world.item.enchantment.Enchantment.getFullname(entry, level);
		}

		@Override
		public String getTranslationKey() {
			return key.identifier().toLanguageKey();
		}

		@Override
		public String getIdentifierKey() {
			return key.identifier().getPath();
		}
	}

}
