package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.EventChatReceive;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;
import java.util.UUID;

@Mixin(MessageHandler.class)
public abstract class MixinMessageHandler {

    @Shadow
    protected abstract PlayerListEntry getPlayerListEntry(UUID uUID);

    @Shadow
    protected abstract MessageTrustStatus getStatus(SignedMessage message, Text decorated, PlayerListEntry senderEntry, Instant instant);

    @Unique
    private EventChatReceive event;

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(SignedMessage signedMessage, MessageType.Parameters params, CallbackInfo ci) {
        Text text = params.applyChatDecoration(signedMessage.getContent());
        Instant instant = Instant.now();

        boolean signed = false;
        boolean expired = signedMessage.isExpiredOnClient(instant);

        MessageMetadata messageMetadata = signedMessage.createMetadata();
        if (!messageMetadata.lacksSender()) {
            PlayerListEntry playerListEntry = this.getPlayerListEntry(messageMetadata.sender());
            MessageTrustStatus messageTrustStatus = this.getStatus(signedMessage, text, playerListEntry, instant);
            signed = !messageTrustStatus.isInsecure();
        }

        this.event = new EventChatReceive(params, signedMessage, expired, signed).broadcast();
        if (this.event.isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "method_44943", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"))
    private void onAddChatMessage(ChatHud instance, Text original, MessageSignatureData signature, MessageIndicator indicator) {
        var arg = this.event.getArg();
        Text message = this.event.getMessage().build();
        Text text = arg.applyChatDecoration(message);
        instance.addMessage(text, signature, indicator);
    }

}
