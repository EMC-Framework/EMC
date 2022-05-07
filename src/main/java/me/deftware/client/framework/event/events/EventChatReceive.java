package me.deftware.client.framework.event.events;

import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.Event;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

import java.time.Instant;
import java.util.UUID;

/**
 * Triggered by Minecraft chat listener at the moment the message is drawn to screen
 */
public class EventChatReceive extends Event {

    @Setter
    @Getter
    private ChatMessage message;

    @Getter
    private final boolean signed, expired;

    private final ChatMessageS2CPacket packet;

    public EventChatReceive(ChatMessageS2CPacket packet, boolean signed) {
        this.message = new ChatMessage().fromText(packet.content());
        this.expired = packet.isExpired(Instant.now());
        this.packet = packet;
        this.signed = signed;
    }

    public ChatMessage getSender() {
        return new ChatMessage().fromText(packet.sender().name());
    }

    public UUID getSenderUUID() {
        return packet.sender().uuid();
    }

}
