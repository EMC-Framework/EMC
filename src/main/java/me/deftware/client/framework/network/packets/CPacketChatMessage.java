package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.NetworkHandler;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.class_9449;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.SignedArgumentList;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.message.*;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class CPacketChatMessage extends PacketWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CPacketChatMessage.class);

    public CPacketChatMessage(Packet<?> packet) {
        super(packet);
    }

    public CPacketChatMessage(String text) {
        super(of(text));
    }

    private static Packet<ServerPlayPacketListener> of(String text) {
        if (text.startsWith("/")) {
            return command(text.substring(1));
        }
        return message(text);
    }

    private static Packet<ServerPlayPacketListener> message(String text) {
        var networkHandler = NetworkHandler.getNetworkHandler();

        Instant instant = Instant.now();
        long salt = NetworkEncryptionUtils.SecureRandomUtil.nextLong();
        LastSeenMessagesCollector.LastSeenMessages lastSeenMessages = networkHandler.collect();
        MessageSignatureData messageSignatureData = networkHandler.pack(new MessageBody(text, instant, salt, lastSeenMessages.lastSeen()));

        return new ChatMessageC2SPacket(text, instant, salt, messageSignatureData, lastSeenMessages.update());
    }

    private static Packet<ServerPlayPacketListener> command(String text) {
        var networkHandler = NetworkHandler.getNetworkHandler();
        var dispatcher = MinecraftClient.getInstance().getNetworkHandler().getCommandDispatcher();
        var source = MinecraftClient.getInstance().getNetworkHandler().getCommandSource();

        var signedArgumentList = SignedArgumentList.of(dispatcher.parse(text, source));
        if (signedArgumentList.arguments().isEmpty()) {
            return new CommandExecutionC2SPacket(text);
        }

        Instant instant = Instant.now();
        long salt = NetworkEncryptionUtils.SecureRandomUtil.nextLong();
        LastSeenMessagesCollector.LastSeenMessages lastSeenMessages = networkHandler.collect();

        ArgumentSignatureDataMap argumentSignatureDataMap = ArgumentSignatureDataMap.sign(signedArgumentList, value -> {
            MessageBody messageBody = new MessageBody(value, instant, salt, lastSeenMessages.lastSeen());
            return networkHandler.pack(messageBody);
        });

        return new class_9449(text, instant, salt, argumentSignatureDataMap, lastSeenMessages.update());
    }

}
