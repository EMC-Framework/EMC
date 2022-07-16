package me.deftware.client.framework.event.events;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.Event;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;

import java.util.UUID;

/**
 * Triggered by Minecraft chat listener at the moment the message is drawn to screen
 */
public class EventChatReceive extends Event {

    private ChatMessage message;

    private final boolean signed, expired;
    private MessageType.Parameters arg;

    private UUID sender;

    public EventChatReceive(MessageType.Parameters arg, SignedMessage message, boolean expired, boolean signed) {
        this.message = new ChatMessage().fromText(message.getContent());
        this.expired = expired;
        this.signed = signed;
        this.sender = message.signedHeader().sender();
        this.arg = arg;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public boolean isSigned() {
        return signed;
    }

    public boolean isExpired() {
        return expired;
    }

    public ChatMessage getSenderName() {
        return new ChatMessage().fromText(arg.name());
    }

    public MessageType.Parameters getArg() {
        return arg;
    }

    public UUID getSenderId() {
        return sender;
    }

    public void setSender(ChatMessage name) {
        this.arg = new MessageType.Parameters(arg.type(), name.build(), arg.targetName());
    }

}
