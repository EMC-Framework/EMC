package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.EventChatReceive;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.MessageSender;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

@Mixin(MessageHandler.class)
public abstract class MixinMessageHandler {

    @Shadow
    protected abstract PlayerListEntry getPlayerListEntry(MessageSender sender);

    @Shadow
    protected abstract MessageTrustStatus getStatus(MessageSender messageSender, SignedMessage message, Text decorated, @Nullable PlayerListEntry senderEntry);

    @Unique
    private EventChatReceive event;

    @Unique
    private MessageType type;

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(MessageType messageType, SignedMessage signedMessage, MessageSender messageSender, CallbackInfo ci) {
        Text text = messageType.chat().apply(signedMessage.getContent(), messageSender);
        this.type = messageType;

        boolean signed = false;
        boolean expired = signedMessage.isExpiredOnClient(Instant.now());

        if (messageSender.hasProfileId()) {
            PlayerListEntry playerListEntry = this.getPlayerListEntry(messageSender);
            MessageTrustStatus messageTrustStatus = this.getStatus(messageSender, signedMessage, text, playerListEntry);
            signed = !messageTrustStatus.isInsecure();
        }

        this.event = new EventChatReceive(messageSender, signedMessage.getContent(), expired, signed).broadcast();
        if (this.event.isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "method_44768", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"))
    private void onAddChatMessage(ChatHud instance, Text original, MessageIndicator indicator) {
        Text message = this.event.getMessage().build();
        Text text = this.type.chat().apply(message, this.event.getSender());
        instance.addMessage(text, indicator);
    }

}
