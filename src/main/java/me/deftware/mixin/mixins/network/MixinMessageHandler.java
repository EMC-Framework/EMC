package me.deftware.mixin.mixins.network;

import com.mojang.authlib.GameProfile;
import me.deftware.client.framework.event.events.EventChatReceive;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

@Mixin(ChatListener.class)
public abstract class MixinMessageHandler {

    @Shadow
    protected abstract ChatTrustLevel evaluateTrustLevel(PlayerChatMessage message, Component decorated, Instant instant);

    @Unique
    private EventChatReceive event;

    @Inject(method = "handlePlayerChatMessage", at = @At("HEAD"), cancellable = true)
    private void onMessage(PlayerChatMessage signedMessage, GameProfile sender, ChatType.Bound params, CallbackInfo ci) {
        Component text = params.decorate(signedMessage.decoratedContent());
        Instant instant = Instant.now();

        boolean signed = false;
        boolean expired = signedMessage.hasExpiredClient(instant);

        if (!signedMessage.isSystem()) {
            var messageTrustStatus = this.evaluateTrustLevel(signedMessage, text, instant);
            signed = !messageTrustStatus.isNotSecure();
        }

        this.event = new EventChatReceive(params, signedMessage, expired, signed).broadcast();
        if (this.event.isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addPlayerMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V"))
    private void onAddMessage(ChatComponent instance, Component original, MessageSignature signature, GuiMessageTag indicator) {
        var arg = this.event.getArg();
        Component message = (Component) this.event.getMessage();
        Component text = arg.decorate(message);
        instance.addPlayerMessage(text, signature, indicator);
    }

}
