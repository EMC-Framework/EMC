package me.deftware.client.framework.util;

import net.minecraft.client.resources.I18n;

/**
 * @author Deftware
 */
public class TranslationUtil {

    public static String translate(String key) {
        return I18n.format(key);
    }

}
