package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.class_7634;
import net.minecraft.class_7635;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.encryption.Signer;
import net.minecraft.network.message.MessageMetadata;
import net.minecraft.network.message.MessageSignatureData;
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
        var messageMeta = MessageMetadata.of(player.getUuid());

        Text literal = Text.literal(text);
        MessageSignatureData signature = MessageSignatureData.EMPTY;
        class_7635.class_7636 lv = networkHandler.method_44941();
        class_7634 lv2 = new class_7634(literal);

        try {
            Signer signer = client.getProfileKeys().getSigner();
            if (signer != null) {
                signature = networkHandler.getMessagePacker().pack(signer, messageMeta, lv2, lv.lastSeen()).signature();
            }
        } catch (Exception ex) {
            LOGGER.error("Unable to sign chat message", ex);
        }

        return new ChatMessageC2SPacket(text, messageMeta.timestamp(), messageMeta.salt(), signature, true, lv);
    }

}
