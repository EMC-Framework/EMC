package me.deftware.client.framework.event.events;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.event.Event;
import net.minecraft.network.message.MessageSender;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.text.Text;

import java.time.Instant;
import java.util.UUID;

/**
 * Triggered by Minecraft chat listener at the moment the message is drawn to screen
 */
public class EventChatReceive extends Event {

    private Message message;

    private final boolean signed, expired;
    private MessageSender sender;

    public EventChatReceive(ChatMessageS2CPacket packet, boolean signed) {
        this.message = (Message) packet.signedContent();
        this.expired = packet.isExpired(Instant.now());
        this.signed = signed;
        this.sender = packet.sender();
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

    public MessageSender getSender() {
        return this.sender;
    }

    public Message getSenderName() {
        return (Message) sender.name();
    }

    public UUID getSenderId() {
        return sender.uuid();
    }

    public void setSender(Message name) {
        this.sender = new MessageSender(this.sender.uuid(), (Text) name);
    }

}
