package me.deftware.client.framework.item.enchantment;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.message.MessageUtils;
import me.deftware.client.framework.registry.Identifiable;
import me.deftware.mixin.imp.IdentifiableResource;

/**
 * @author Deftware
 */
public class Enchantment implements Identifiable {

	protected final net.minecraft.enchantment.Enchantment enchantment;

	public Enchantment(net.minecraft.enchantment.Enchantment enchantment) {
		this.enchantment = enchantment;
	}

	public net.minecraft.enchantment.Enchantment getMinecraftEnchantment() {
		return enchantment;
	}

	@Override
	public String getTranslationKey() {
		return enchantment.getName();
	}

	@Override
	public String getIdentifierKey() {
		return ((IdentifiableResource) enchantment).getResourceLocation().getResourcePath();
	}

	public int getMinLevel() {
		return enchantment.getMinLevel();
	}

	public int getMaxLevel() {
		return enchantment.getMaxLevel();
	}

	public Message getName(int level) {
		return MessageUtils.parse(enchantment.getTranslatedName(level));
	}

}
