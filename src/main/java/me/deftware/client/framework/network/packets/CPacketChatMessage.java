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
        var networkHandler = client.getNetworkHandler();
        var chatMessageSigner = ChatMessageSigner.create(player.getUuid());

        Text literal = Text.literal(text);
        MessageSignature signature = MessageSignature.field_39811;

        try {
            Signer signer = client.getProfileKeys().getSigner();
            if (signer != null) {
                signature = networkHandler.method_44816().pack(signer, chatMessageSigner, literal).signature();
            }
        } catch (Exception ex) {
            LOGGER.error("Unable to sign chat message", ex);
        }

        return new ChatMessageC2SPacket(text, chatMessageSigner.timeStamp(), chatMessageSigner.salt(), signature, true);
    }

}
