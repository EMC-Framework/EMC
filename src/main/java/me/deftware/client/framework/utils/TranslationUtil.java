package me.deftware.client.framework.utils;

import net.minecraft.client.resources.I18n;

public class TranslationUtil {

    public static String translate(String key) {
        return I18n.format(key);
    }

}
