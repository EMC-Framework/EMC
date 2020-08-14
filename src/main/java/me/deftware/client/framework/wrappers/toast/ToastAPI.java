package me.deftware.client.framework.wrappers.toast;

import net.minecraft.client.Minecraft;

/**
 * A wrapper for sending toasts using the minecraft toast system
 *
 * @author Deftware
 */
public class ToastAPI {

	public static void addToast(ToastImpl toast) {
		Minecraft.getInstance().getToastGui().add(toast);
	}

}
