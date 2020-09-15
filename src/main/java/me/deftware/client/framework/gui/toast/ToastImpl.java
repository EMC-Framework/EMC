package me.deftware.client.framework.gui.toast;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.chat.builder.ChatBuilder;
import me.deftware.client.framework.render.texture.Texture;

/**
 * A custom toast implementation that can be used by EMC mods
 *
 * @author Deftware
 */
public class ToastImpl {

	public Texture icon;
	public ChatMessage title;
	public ChatMessage[] text;
	public long transitionTime = 2000L, visibilityTime = transitionTime;
	public int width = 160, height = 32, index = 0;
	public int iconWidth = 25, iconHeight = 25;

	public ToastImpl(ChatMessage title, ChatMessage... text) {
		this(null, title, text);
		// Toasts does not exist in <1.12
		new ChatBuilder().withPrefix().withMessage(title).build().print();
		for (ChatMessage line : text) {
			new ChatBuilder().withPrefix().withMessage(line).build().print();
		}
	}

	public ToastImpl(Texture icon, ChatMessage title, ChatMessage... text) {
		this.text = text;
		this.title = title;
		this.icon = icon;
		if (text != null && text.length > 0) {
			visibilityTime *= text.length;
		}
	}

}

