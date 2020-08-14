package me.deftware.client.framework.chat.hud;

import me.deftware.client.framework.chat.ChatMessage;

/**
 * @author Deftware
 */
public class ChatHudLine {
	private ChatMessage message;
	private int lineId;

	public ChatHudLine(final ChatMessage message, final int lineId) {
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
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof ChatHudLine)) return false;
		final ChatHudLine other = (ChatHudLine) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$message = this.getMessage();
		final Object other$message = other.getMessage();
		if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
		if (this.getLineId() != other.getLineId()) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof ChatHudLine;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $message = this.getMessage();
		result = result * PRIME + ($message == null ? 43 : $message.hashCode());
		result = result * PRIME + this.getLineId();
		return result;
	}

	@Override
	public String toString() {
		return "ChatHudLine(message=" + this.getMessage() + ", lineId=" + this.getLineId() + ")";
	}
}
