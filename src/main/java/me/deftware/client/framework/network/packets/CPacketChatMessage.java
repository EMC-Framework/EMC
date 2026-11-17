package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.NetworkHandler;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.chat.LastSeenMessagesTracker;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignableCommand;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.util.Crypt;
import java.time.Instant;

public class CPacketChatMessage extends PacketWrapper {

    public CPacketChatMessage(Packet<?> packet) {
        super(packet);
    }

    public CPacketChatMessage(String text) {
        super(of(text));
    }

    private static Packet<ServerGamePacketListener> of(String text) {
        if (text.startsWith("/")) {
            return command(text.substring(1));
        }
        return message(text);
    }

    private static Packet<ServerGamePacketListener> message(String text) {
        var networkHandler = NetworkHandler.getNetworkHandler();

        Instant instant = Instant.now();
        long salt = Crypt.SaltSupplier.getLong();
        LastSeenMessagesTracker.Update lastSeenMessages = networkHandler.collect();
        MessageSignature messageSignatureData = networkHandler.pack(new SignedMessageBody(text, instant, salt, lastSeenMessages.lastSeen()));

        return new ServerboundChatPacket(text, instant, salt, java.util.Optional.ofNullable(messageSignatureData), lastSeenMessages.update());
    }

    private static Packet<ServerGamePacketListener> command(String text) {
        var networkHandler = NetworkHandler.getNetworkHandler();
        var dispatcher = Minecraft.getInstance().getConnection().getCommands();
        var source = Minecraft.getInstance().getConnection().getSuggestionsProvider();

        var signedArgumentList = SignableCommand.of(dispatcher.parse(text, source));
        if (signedArgumentList.arguments().isEmpty()) {
            return new ServerboundChatCommandPacket(text);
        }

        Instant instant = Instant.now();
        long salt = Crypt.SaltSupplier.getLong();
        LastSeenMessagesTracker.Update lastSeenMessages = networkHandler.collect();

        ArgumentSignatures argumentSignatureDataMap = ArgumentSignatures.signCommand(signedArgumentList, value -> {
            SignedMessageBody messageBody = new SignedMessageBody(value, instant, salt, lastSeenMessages.lastSeen());
            return networkHandler.pack(messageBody);
        });

        return new ServerboundChatCommandSignedPacket(text, instant, salt, argumentSignatureDataMap, lastSeenMessages.update());
    }

}
