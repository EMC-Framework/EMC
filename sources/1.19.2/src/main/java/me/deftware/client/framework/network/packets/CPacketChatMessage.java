package me.deftware.client.framework.network.packets;

import com.mojang.brigadier.ParseResults;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DecoratableArgumentList;
import net.minecraft.network.Packet;
import net.minecraft.network.encryption.Signer;
import net.minecraft.network.message.*;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPacketChatMessage extends PacketWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CPacketChatMessage.class);

    public CPacketChatMessage(Packet<?> packet) {
        super(packet);
    }

    public CPacketChatMessage(String text) {
        super(of(text));
    }

    public static Packet<?> of(String text) {
        if (text.startsWith("/")) {
            return command(text.substring(1));
        }
        return message(text);
    }

    public static ChatMessageC2SPacket message(String text) {
        var client = MinecraftClient.getInstance();
        var player = client.player;
        var networkHandler = client.getNetworkHandler();
        var messageMeta = MessageMetadata.of(player.getUuid());

        Text literal = Text.literal(text);
        MessageSignatureData signature = MessageSignatureData.EMPTY;
        LastSeenMessageList.Acknowledgment acknowledgment = networkHandler.consumeAcknowledgment();
        DecoratedContents decoratedContents = new DecoratedContents(text, literal);

        try {
            Signer signer = client.getProfileKeys().getSigner();
            if (signer != null) {
                signature = networkHandler.getMessagePacker().pack(signer, messageMeta, decoratedContents, acknowledgment.lastSeen()).signature();
            }
        } catch (Exception ex) {
            LOGGER.error("Unable to sign chat message", ex);
        }

        return new ChatMessageC2SPacket(text, messageMeta.timestamp(), messageMeta.salt(), signature, true, acknowledgment);
    }

    public static CommandExecutionC2SPacket command(String text) {
        var client = MinecraftClient.getInstance();
        var networkHandler = client.getNetworkHandler();
        var preview = Text.of(text);
        var player = client.player;

        MessageMetadata messageMetadata = MessageMetadata.of(player.getUuid());
        LastSeenMessageList.Acknowledgment acknowledgment = networkHandler.consumeAcknowledgment();

        ParseResults<CommandSource> parseResults = networkHandler.getCommandDispatcher().parse(text, networkHandler.getCommandSource());
        var argumentSignatureDataMap = signArguments(messageMetadata, acknowledgment.lastSeen(), parseResults, preview);

        return new CommandExecutionC2SPacket(text, messageMetadata.timestamp(), messageMetadata.salt(), argumentSignatureDataMap, true, acknowledgment);
    }

    private static ArgumentSignatureDataMap signArguments(MessageMetadata messageMetadata, LastSeenMessageList lastSeen, ParseResults<CommandSource> parseResults, Text preview) {
        var client = MinecraftClient.getInstance();
        var networkHandler = client.getNetworkHandler();
        Signer profileSigner = client.getProfileKeys().getSigner();
        if (profileSigner == null) {
            return ArgumentSignatureDataMap.EMPTY;
        }
        try {
            return ArgumentSignatureDataMap.sign(DecoratableArgumentList.of(parseResults), (argumentName, value) -> {
                DecoratedContents decoratedContents = preview != null ? new DecoratedContents(value, preview) : new DecoratedContents(value);
                return networkHandler.getMessagePacker().pack(profileSigner, messageMetadata, decoratedContents, lastSeen).signature();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            return ArgumentSignatureDataMap.EMPTY;
        }
    }


}
