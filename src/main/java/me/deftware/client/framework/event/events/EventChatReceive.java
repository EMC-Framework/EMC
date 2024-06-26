package me.deftware.client.framework.event.events;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.event.Event;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;

import java.util.UUID;

/**
 * Triggered by Minecraft chat listener at the moment the message is drawn to screen
 */
public class EventChatReceive extends Event {

    private Message message;

    private final boolean signed, expired;
    private MessageType.Parameters arg;

    private final UUID sender;

    public EventChatReceive(MessageType.Parameters arg, SignedMessage message, boolean expired, boolean signed) {
        this.message = (Message) message.getContent();
        this.expired = expired;
        this.signed = signed;
        this.sender = message.getSender();
        this.arg = arg;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public boolean isSigned() {
        return signed;
    }

    public boolean isExpired() {
        return expired;
    }

    public Message getSenderName() {
        return (Message) arg.name();
    }

    public MessageType.Parameters getArg() {
        return arg;
    }

    public UUID getSenderId() {
        return sender;
    }

    public void setSender(Message name) {
        this.arg = new MessageType.Parameters(arg.type(), (Text) name, arg.targetName());
    }

}
