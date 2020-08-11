package me.deftware.client.framework.wrappers.item;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

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
        return new ChatMessage().fromText(new TextComponentTranslation(item.getName()), false);
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
        ResourceLocation resourceLocation = new ResourceLocation(id);
        if (Enchantment.REGISTRY.containsKey(resourceLocation)) {
            return new IEnchantment(Enchantment.REGISTRY.get(resourceLocation));
        } else {
            for (Object enchantment : Enchantment.REGISTRY) {
                IEnchantment enchantmentObj = new IEnchantment((Enchantment)enchantment);
                if (enchantmentObj.getEnchantmentKey().equalsIgnoreCase(id)) {
                    return enchantmentObj;
                }
            }
        }
        return null;
    }

}
