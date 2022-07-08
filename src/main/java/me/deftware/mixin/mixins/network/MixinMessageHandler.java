package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.EventChatReceive;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
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
    protected abstract PlayerListEntry method_44731(UUID uUID);

    @Shadow
    protected abstract MessageTrustStatus method_44732(SignedMessage signedMessage, Text text, @Nullable PlayerListEntry playerListEntry);

    @Unique
    private EventChatReceive event;

    @Inject(method = "method_44733", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(SignedMessage signedMessage, MessageType.class_7602 arg, CallbackInfo ci) {
        Text text = arg.method_44837(signedMessage.getContent());

        boolean signed = false;
        boolean expired = signedMessage.isExpiredOnClient(Instant.now());

        ChatMessageSigner chatMessageSigner = signedMessage.method_44866();
        if (!chatMessageSigner.method_44851()) {
            PlayerListEntry playerListEntry = this.method_44731(chatMessageSigner.profileId());
            MessageTrustStatus messageTrustStatus = this.method_44732(signedMessage, text, playerListEntry);
            signed = !messageTrustStatus.isInsecure();
        }

        this.event = new EventChatReceive(arg, signedMessage, expired, signed).broadcast();
        if (this.event.isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "method_44768", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;method_44811(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignature;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"))
    private void onAddChatMessage(ChatHud instance, Text original, MessageSignature messageSignature, MessageIndicator messageIndicator) {
        var arg = this.event.getArg();
        Text message = this.event.getMessage().build();
        Text text = arg.method_44837(message);
        instance.method_44811(text, messageSignature, messageIndicator);
    }

}
