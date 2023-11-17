package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.command.CustomSuggestionProvider;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketChatMessage;
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

    @Shadow
    private boolean prevOnGround;

    @Shadow
    private float horseJumpPower;

    @Shadow
    public abstract boolean isHandActive();

    @Shadow
    @Final
    public NetHandlerPlayClient connection;

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

    @Unique
    private final EventSlowdown eventSlowdown = new EventSlowdown();

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "net/minecraft/client/entity/EntityPlayerSP.isHandActive()Z", ordinal = 0))
    private boolean itemUseSlowdownEvent(EntityPlayerSP self) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Item_Use, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return false;
        }
        return isHandActive();
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "net/minecraft/util/FoodStats.getFoodLevel()I"))
    private int hungerSlowdownEvent(FoodStats self) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Hunger, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return 7;
        }
        return self.getFoodLevel();
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    private boolean blindlessSlowdownEvent(EntityPlayerSP self, Potion potion) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Blindness, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return false;
        }
        return self.isPotionActive(MobEffects.BLINDNESS);
    }

    @Unique
    private final EventUpdate eventUpdate = new EventUpdate();

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        EntityPlayerSP entity = (EntityPlayerSP) (Object) this;
        eventUpdate.create(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch, entity.onGround);
        eventUpdate.broadcast();
        if (eventUpdate.isCanceled()) {
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
            if (!message.startsWith(trigger)) {
                String text = event.getMessage();
                if (event.getType() == EventChatSend.Type.Command) {
                    text = "/" + text;
                }
                connection.sendPacket(new CPacketChatMessage(text));
            } else {
                try {
                    CommandRegister.getDispatcher().execute(message.substring(CommandRegister.getCommandTrigger().length()), new CustomSuggestionProvider());
                } catch (Exception ex) {
                    Bootstrap.logger.error("Failed to execute command", ex);
                    Message.of("Failed to execute command: " + ex.getMessage()).print();
                }
            }
        }
    }

    @Override
    public void setHorseJumpPower(float height) {
        horseJumpPower = height;
    }

    @Unique
    private final EventPlayerWalking eventPlayerWalking = new EventPlayerWalking();

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "HEAD"), cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        EntityPlayerSP entity = (EntityPlayerSP) (Object) this;
        eventPlayerWalking.create(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch, onGround);
        eventPlayerWalking.broadcast();
        if (eventPlayerWalking.isCanceled()) {
            ci.cancel();
        }
    }

    @Unique
    private final EventPlayerWalking.PostEvent postEvent = new EventPlayerWalking.PostEvent();

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "TAIL"), cancellable = true)
    private void onSendMovementPacketsTail(CallbackInfo ci) {
        EntityPlayerSP entity = (EntityPlayerSP) (Object) this;
        postEvent.create(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch, onGround);
        postEvent.broadcast();
        if (postEvent.isCanceled()) {
            ci.cancel();
        }
    }

}
