package me.deftware.client.framework.chat;

import me.deftware.client.framework.chat.style.ChatStyle;

/**
 * @author Deftware
 */
public class ChatSection {
	private ChatStyle style = new ChatStyle();
	private String text;

	public ChatSection(String text) {
		this.text = text;
	}

	public ChatSection reset() {
		style = new ChatStyle();
		text = "";
		return this;
	}

	public ChatStyle getStyle() {
		return this.style;
	}

	public void setStyle(final ChatStyle style) {
		this.style = style;
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}
}
