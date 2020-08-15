package me.deftware.client.framework.wrappers.item;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ChatComponentTranslation;

public class IEnchantment {

    private Enchantment item;

    public IEnchantment(String name) {
        item = getByName(name).item;
    }

    public IEnchantment(Enchantment item) {
        this.item = item;
    }

    public Enchantment getEnchantment() {
        return item;
    }

    public ChatMessage getName() {
        return new ChatMessage().fromText(new ChatComponentTranslation(item.getName()), false);
    }

    public String getTranslationKey() {
        return item.getName();
    }

    public String getEnchantmentKey() {
        String key = getTranslationKey();
        if (key.startsWith("enchantment.minecraft")) {
            key = key.substring("enchantment.minecraft.".length());
        }
        return key;
    }

    public boolean isValidEnchant() {
        return item != null;
    }

    public static IEnchantment getByName(String id) {
        for (Object enchantment : Enchantment.enchantmentsBookList) {
            IEnchantment enchantmentObj = new IEnchantment((Enchantment)enchantment);
            if (enchantmentObj.getEnchantmentKey().equalsIgnoreCase(id)) {
                return enchantmentObj;
            }
        }
        return null;
    }

}
