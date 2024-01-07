package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.EventChatSend;
import me.deftware.client.framework.minecraft.Chat;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public class MixinChatScreen {

    @Redirect(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatMessage(Ljava/lang/String;)V"))
    private void onMessage$Chat(ClientPlayNetworkHandler networkHandler, String content) {
        Chat.send(networkHandler::sendChatMessage, content, ClientPlayerEntity.class, EventChatSend.Type.Message);
    }

    @Redirect(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatCommand(Ljava/lang/String;)V"))
    private void onMessage$Command(ClientPlayNetworkHandler networkHandler, String content) {
        Chat.send(networkHandler::sendChatCommand, content, ClientPlayerEntity.class, EventChatSend.Type.Command);
    }

}
