package me.deftware.client.framework.event.events;

import me.deftware.client.framework.message.Message;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import me.deftware.client.framework.event.Event;
import java.util.UUID;

/**
 * Triggered by Minecraft chat listener at the moment the message is drawn to screen
 */
public class EventChatReceive extends Event {

    private Message message;

    private final boolean signed, expired;
    private ChatType.Bound arg;

    private final UUID sender;

    public EventChatReceive(ChatType.Bound arg, PlayerChatMessage message, boolean expired, boolean signed) {
        this.message = (Message) message.decoratedContent();
        this.expired = expired;
        this.signed = signed;
        this.sender = message.sender();
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

    public ChatType.Bound getArg() {
        return arg;
    }

    public UUID getSenderId() {
        return sender;
    }

    public void setSender(Message name) {
        this.arg = new ChatType.Bound(arg.chatType(), (Component) name, arg.targetName());
    }

}
