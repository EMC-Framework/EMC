package me.deftware.client.framework.wrappers.toast;

import me.deftware.client.framework.chat.ChatBuilder;

/**
 * A wrapper for sending toasts using the minecraft toast system
 *
 * @author Deftware
 */
public class ToastAPI {

	public static void addToast(ToastImpl toast) {
		// TODO: Fix in <1.12
		new ChatBuilder().withPrefix().withText("Alert -> " + toast.title).build().print();
		for (String toastTextChunk : toast.text) {
			new ChatBuilder().withPrefix().withText("- " + toastTextChunk).build().print();
		}
	}

}
