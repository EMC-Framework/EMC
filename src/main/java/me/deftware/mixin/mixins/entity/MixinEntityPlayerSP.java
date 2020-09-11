package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.chat.builder.ChatBuilder;
import me.deftware.client.framework.chat.style.ChatColors;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinEntityPlayerSP extends MixinEntity implements IMixinEntityPlayerSP {

    @Shadow
    @Final
    protected MinecraftClient client;

    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;

    @Shadow
    private float field_3922;

    @Shadow
    public abstract boolean isUsingItem();

    @Inject(method = "closeContainer", at = @At("HEAD"))
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

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.isUsingItem()Z", ordinal = 0))
    private boolean itemUseSlowdownEvent(ClientPlayerEntity self) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Item_Use);
        event.broadcast();
        if (event.isCanceled()) {
            return false;
        }
        return isUsingItem();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/HungerManager.getFoodLevel()I"))
    private int hungerSlowdownEvent(HungerManager self) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Hunger);
        event.broadcast();
        if (event.isCanceled()) {
            return 7;
        }
        return self.getFoodLevel();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    private boolean onBlindnessSlowdown(ClientPlayerEntity self, StatusEffect effect) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Blindness);
        event.broadcast();
        if (event.isCanceled()) {
            return false;
        }
        return self.hasStatusEffect(effect);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        EventUpdate event = new EventUpdate(((ClientPlayerEntity) (Object) this).x, ((ClientPlayerEntity) (Object) this).y, ((ClientPlayerEntity) (Object) this).z, yaw, pitch, onGround);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        sendChatMessageWithSender(message, ClientPlayerEntity.class);
        ci.cancel();
    }

    @Unique
    public void sendChatMessageWithSender(String message, Class<?> sender) {
        String trigger = CommandRegister.getCommandTrigger();
        EventChatSend event = new EventChatSend(message, sender).broadcast();
        if (!event.isCanceled()) {
            if (event.isDispatch() || !message.startsWith(trigger)) {
                networkHandler.sendPacket(new ChatMessageC2SPacket(event.getMessage()));
            } else {
                try {
                    CommandRegister.getDispatcher().execute(message.substring(CommandRegister.getCommandTrigger().length()), MinecraftClient.getInstance().player.getCommandSource());
                } catch (Exception ex) {
                    Bootstrap.logger.error("Failed to execute command", ex);
                    new ChatBuilder().withText(ex.getMessage()).withColor(ChatColors.RED).build().print();
                }
            }
        }
    }

    @Override
    public void setHorseJumpPower(float height) {
        field_3922 = height;
    }

    @Inject(method = "sendMovementPackets", at = @At(value = "HEAD"), cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        ClientPlayerEntity entity = (ClientPlayerEntity) (Object) this;
        EventPlayerWalking event = new EventPlayerWalking(entity.x, entity.y, entity.z, yaw, pitch, onGround);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

}
