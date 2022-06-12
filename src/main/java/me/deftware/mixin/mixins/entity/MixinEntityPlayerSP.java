package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.chat.LiteralChatMessage;
import me.deftware.client.framework.chat.style.ChatColors;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.minecraft.Chat;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.network.message.ChatMessageSigner;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinEntityPlayerSP extends MixinEntity implements IMixinEntityPlayerSP, Chat {

    @Shadow
    @Final
    protected MinecraftClient client;

    @Shadow
    private float mountJumpStrength;

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    protected abstract void sendChatMessagePacket(ChatMessageSigner signer, String message, @Nullable Text preview);

    @Shadow
    protected abstract void sendCommand(ChatMessageSigner signer, String command, @Nullable Text preview);

    @Inject(method = "closeHandledScreen", at = @At("HEAD"))
    private void onCloseHandledScreen(CallbackInfo ci) {
        new EventGuiContainerClose().broadcast();
    }
    
    @Inject(at = @At("HEAD"), cancellable = true, method = "isCamera")
    public void isCamera(CallbackInfoReturnable<Boolean> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Unique
    private final EventSlowdown eventSlowdown = new EventSlowdown();

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.isUsingItem()Z", ordinal = 0))
    private boolean itemUseSlowdownEvent(ClientPlayerEntity self) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Item_Use, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return false;
        }
        return isUsingItem();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/HungerManager.getFoodLevel()I"))
    private int hungerSlowdownEvent(HungerManager self) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Hunger, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return 7;
        }
        return self.getFoodLevel();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    private boolean onBlindnessSlowdown(ClientPlayerEntity self, StatusEffect effect) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Blindness, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return false;
        }
        return self.hasStatusEffect(effect);
    }

    @Unique
    private final EventUpdate eventUpdate = new EventUpdate();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().player != null) {
            eventUpdate.create(((ClientPlayerEntity) (Object) this).getX(), ((ClientPlayerEntity) (Object) this).getY(), ((ClientPlayerEntity) (Object) this).getZ(), getYaw(), getPitch(), onGround);
            eventUpdate.broadcast();
            if (eventUpdate.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Override
    public void setHorseJumpPower(float height) {
        mountJumpStrength = height; // TODO: Verify
    }

    @Unique
    private final EventPlayerWalking eventPlayerWalking = new EventPlayerWalking();

    @Inject(method = "sendMovementPackets", at = @At(value = "HEAD"), cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        ClientPlayerEntity entity = (ClientPlayerEntity) (Object) this;
        eventPlayerWalking.create(entity.getX(), entity.getY(), entity.getZ(), getYaw(), getPitch(), onGround);
        eventPlayerWalking.broadcast();
        if (eventPlayerWalking.isCanceled()) {
            ci.cancel();
        }
    }

    @Unique
    private final EventPlayerWalking.PostEvent postEvent = new EventPlayerWalking.PostEvent();

    @Inject(method = "sendMovementPackets", at = @At(value = "TAIL"), cancellable = true)
    private void onSendMovementPacketsTail(CallbackInfo ci) {
        ClientPlayerEntity entity = (ClientPlayerEntity) (Object) this;
        postEvent.create(entity.getX(), entity.getY(), entity.getZ(), getYaw(), getPitch(), onGround);
        postEvent.broadcast();
        if (postEvent.isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessagePacket(Lnet/minecraft/network/message/ChatMessageSigner;Ljava/lang/String;Lnet/minecraft/text/Text;)V"))
    private void onMessage(ClientPlayerEntity instance, ChatMessageSigner signer, String message, Text preview) {
        this.send(this::sendChatMessagePacket, message, preview, ClientPlayerEntity.class, EventChatSend.Type.Message);
    }

    @Redirect(method = "sendCommand(Ljava/lang/String;Lnet/minecraft/text/Text;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendCommand(Lnet/minecraft/network/message/ChatMessageSigner;Ljava/lang/String;Lnet/minecraft/text/Text;)V"))
    private void onCommand(ClientPlayerEntity instance, ChatMessageSigner signer, String command, Text preview) {
        this.send(this::sendCommand, command, preview, ClientPlayerEntity.class, EventChatSend.Type.Command);
    }

    @Unique
    @Override
    public void message(String text, Class<?> sender) {
        this.send(this::sendChatMessagePacket, text, Text.literal(text), sender, EventChatSend.Type.Message);
    }

    @Unique
    @Override
    public void command(String text, Class<?> sender) {
        this.send(this::sendCommand, text, Text.literal(text), sender, EventChatSend.Type.Command);
    }

    @Unique
    public void send(Chat.Consumer consumer, String text, Text preview, Class<?> sender, EventChatSend.Type type) {
        EventChatSend event = new EventChatSend(text, sender, type).broadcast();
        if (!event.isCanceled()) {
            // Client command hook
            String trigger = CommandRegister.getCommandTrigger();
            if (text.startsWith(trigger)) {
                text = text.substring(trigger.length());
                try {
                    CommandRegister.getDispatcher().execute(text, MinecraftClient.getInstance().player.getCommandSource());
                } catch (Exception ex) {
                    new LiteralChatMessage(ex.getMessage(), ChatColors.RED).print();
                }
                return;
            }
            // Update text and preview if the message has changed
            if (!event.getMessage().equalsIgnoreCase(text)) {
                text = event.getMessage();
                preview = Text.literal(text);
            }
            ChatMessageSigner chatMessageSigner = ChatMessageSigner.create(this.getUuid());
            consumer.apply(chatMessageSigner, text, preview);
        }
    }

}
