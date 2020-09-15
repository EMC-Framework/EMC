package me.deftware.client.framework.item.enchantment;

import me.deftware.client.framework.chat.ChatMessage;

/**
 * @author Deftware
 */
public class Enchantment {

	protected final net.minecraft.enchantment.Enchantment enchantment;

	public Enchantment(net.minecraft.enchantment.Enchantment enchantment) {
		this.enchantment = enchantment;
	}

	public net.minecraft.enchantment.Enchantment getMinecraftEnchantment() {
		return enchantment;
	}

	public String getUnlocalizedName() {
		return enchantment.getName();
	}

	public String getEnchantmentKey() {
		String key = getUnlocalizedName();
		if (key.startsWith("enchantment.minecraft")) {
			key = key.substring("enchantment.minecraft.".length());
		}
		return key;
	}

	public int getMinLevel() {
		return enchantment.getMinLevel();
	}

	public int getMaxLevel() {
		return enchantment.getMaxLevel();
	}

	public ChatMessage getName(int level) {
		return new ChatMessage().fromString(enchantment.getTranslatedName(level));
	}

}
