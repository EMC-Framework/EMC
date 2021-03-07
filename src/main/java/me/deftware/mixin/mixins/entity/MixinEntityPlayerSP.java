package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.chat.builder.ChatBuilder;
import me.deftware.client.framework.chat.style.ChatColors;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.command.CustomSuggestionProvider;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinEntity implements IMixinEntityPlayerSP {

    //@Shadow
    private boolean prevOnGround;

    @Shadow
    private float horseJumpPower;

    @Shadow
    public abstract boolean isCurrentViewEntity();

    @Shadow
    @Final
    public NetHandlerPlayClient sendQueue;

    @Inject(method = "closeScreen", at = @At("HEAD"))
    private void onCloseHandledScreen(CallbackInfo ci) {
        new EventGuiContainerClose().broadcast();
    }
    
    @Inject(at = @At("HEAD"), cancellable = true, method = "isCurrentViewEntity")
    public void isCamera(CallbackInfoReturnable<Boolean> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "net/minecraft/client/entity/EntityPlayerSP.isHandActive()Z", ordinal = 0))
    private boolean itemUseSlowdownEvent(EntityPlayerSP self) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Item_Use);
        event.broadcast();
        if (event.isCanceled()) {
            return false;
        }
        return self.isUsingItem();
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "net/minecraft/util/FoodStats.getFoodLevel()I"))
    private int hungerSlowdownEvent(FoodStats self) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Hunger);
        event.broadcast();
        if (event.isCanceled()) {
            return 7;
        }
        return self.getFoodLevel();
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    private boolean blindlessSlowdownEvent(EntityPlayerSP self, Potion potion) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Blindness);
        event.broadcast();
        if (event.isCanceled()) {
            return false;
        }
        return self.isPotionActive(Potion.blindness);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        EntityPlayerSP entity = (EntityPlayerSP) (Object) this;
        EventUpdate event = new EventUpdate(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch, entity.onGround);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }


    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        sendChatMessageWithSender(message, EntityPlayerSP.class);
        ci.cancel();
    }

    @Unique
    public void sendChatMessageWithSender(String message, Class<?> sender) {
        String trigger = CommandRegister.getCommandTrigger();
        EventChatSend event = new EventChatSend(message, sender).broadcast();
        if (!event.isCanceled()) {
            if (event.isDispatch() || !message.startsWith(trigger)) {
                sendQueue.addToSendQueue(new C01PacketChatMessage(event.getMessage()));
            } else {
                try {
                    CommandRegister.getDispatcher().execute(message.substring(CommandRegister.getCommandTrigger().length()), new CustomSuggestionProvider());
                } catch (Exception ex) {
                    Bootstrap.logger.error("Failed to execute command", ex);
                    new ChatBuilder().withText(ex.getMessage()).withColor(ChatColors.RED).build().print();
                }
            }
        }
    }

    @Override
    public void setHorseJumpPower(float height) {
        horseJumpPower = height;
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "HEAD"), cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        EntityPlayerSP entity = (EntityPlayerSP) (Object) this;
        EventPlayerWalking event = new EventPlayerWalking(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch, entity.onGround);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "TAIL"), cancellable = true)
    private void onSendMovementPacketsTail(CallbackInfo ci) {
        EntityPlayerSP entity = (EntityPlayerSP) (Object) this;
        EventPlayerWalking event = new EventPlayerWalking.PostEvent(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch, entity.onGround);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }


}
