package me.deftware.client.framework.chat.hud;

import me.deftware.client.framework.chat.ChatMessage;

/**
 * @author Deftware
 */
public class HudLine {
	private ChatMessage message;
	private int lineId;

	public HudLine(final ChatMessage message, final int lineId) {
		this.message = message;
		this.lineId = lineId;
	}

	public ChatMessage getMessage() {
		return this.message;
	}

	public int getLineId() {
		return this.lineId;
	}

	public void setMessage(final ChatMessage message) {
		this.message = message;
	}

	public void setLineId(final int lineId) {
		this.lineId = lineId;
	}

	@Override
	public String toString() {
		return "ChatHudLine(message=" + this.getMessage() + ", lineId=" + this.getLineId() + ")";
	}
}
