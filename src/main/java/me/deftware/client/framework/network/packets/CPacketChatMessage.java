package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.encryption.Signer;
import net.minecraft.network.message.ChatMessageSigner;
import net.minecraft.network.message.MessageSignature;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPacketChatMessage extends PacketWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CPacketChatMessage.class);

    public CPacketChatMessage(Packet<?> packet) {
        super(packet);
    }

    public CPacketChatMessage(String text) {
        super(message(text));
    }

    public static ChatMessageC2SPacket message(String text) {
        var client = MinecraftClient.getInstance();
        var player = client.player;
        var uuid = player.getUuid();
        ChatMessageSigner chatMessageSigner = ChatMessageSigner.create(uuid);

        Text literal = Text.literal(text);
        MessageSignature signature = MessageSignature.none(uuid);

        try {
            Signer signer = client.getProfileKeys().getSigner();
            if (signer != null) {
                signature = chatMessageSigner.sign(signer, literal);
            }
        } catch (Exception ex) {
            LOGGER.error("Unable to sign chat message", ex);
        }

        return new ChatMessageC2SPacket(text, signature, true);
    }

}
