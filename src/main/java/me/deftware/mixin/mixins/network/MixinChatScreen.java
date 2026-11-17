package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.EventChatSend;
import me.deftware.client.framework.minecraft.Chat;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public class MixinChatScreen {

    @Redirect(method = "handleChatInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;sendChat(Ljava/lang/String;)V"))
    private void onMessage$Chat(ClientPacketListener networkHandler, String content) {
        Chat.send(networkHandler::sendChat, content, LocalPlayer.class, EventChatSend.Type.Message);
    }

    @Redirect(method = "handleChatInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;sendCommand(Ljava/lang/String;)V"))
    private void onMessage$Command(ClientPacketListener networkHandler, String content) {
        Chat.send(networkHandler::sendCommand, content, LocalPlayer.class, EventChatSend.Type.Command);
    }

}
