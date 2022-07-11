package me.deftware.client.framework.network.packets;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.ParseResults;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.network.Packet;
import net.minecraft.network.encryption.Signer;
import net.minecraft.network.message.ArgumentSignatureDataMap;
import net.minecraft.network.message.ChatMessageSigner;
import net.minecraft.network.message.MessageSignature;
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
        ChatMessageSigner chatMessageSigner = ChatMessageSigner.create(player.getUuid());

        Text literal = Text.literal(text);
        MessageSignature signature = MessageSignature.none();

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

    public static CommandExecutionC2SPacket command(String text) {
        var client = MinecraftClient.getInstance();
        var networkHandler = client.getNetworkHandler();
        var preview = Text.of(text);
        var player = client.player;

        ChatMessageSigner chatMessageSigner = ChatMessageSigner.create(player.getUuid());

        ParseResults<CommandSource> parseResults = networkHandler.getCommandDispatcher().parse(text, networkHandler.getCommandSource());
        var argumentSignatureDataMap = signArguments(chatMessageSigner, parseResults, preview);

        return new CommandExecutionC2SPacket(text, chatMessageSigner.timeStamp(), argumentSignatureDataMap, true);
    }

    private static ArgumentSignatureDataMap signArguments(ChatMessageSigner signer, ParseResults<CommandSource> parseResults, Text preview) {
        var client = MinecraftClient.getInstance();
        var map = ArgumentSignatureDataMap.collectArguments(parseResults.getContext());
        Signer profileSigner = client.getProfileKeys().getSigner();
        if (map.isEmpty() || profileSigner == null) {
            return ArgumentSignatureDataMap.empty();
        }
        try {
            ImmutableMap.Builder<String, byte[]> builder = ImmutableMap.builder();
            map.forEach((argumentName, value) -> {
                Text text2 = preview != null ? preview : value;
                MessageSignature messageSignature = signer.sign(profileSigner, text2);
                builder.put(argumentName, messageSignature.saltSignature().signature());
            });
            return new ArgumentSignatureDataMap(signer.salt(), builder.build());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ArgumentSignatureDataMap.empty();
        }
    }

}
