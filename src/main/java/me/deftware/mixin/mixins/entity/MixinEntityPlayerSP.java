package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.minecraft.Chat;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class MixinEntityPlayerSP extends MixinEntity implements IMixinEntityPlayerSP, Chat {

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    private float jumpRidingScale;

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    @Final
    public ClientPacketListener connection;

    @Inject(method = "closeContainer", at = @At("HEAD"))
    private void onCloseHandledScreen(CallbackInfo ci) {
        new EventGuiContainerClose().broadcast();
    }
    
    @Inject(at = @At("HEAD"), cancellable = true, method = "isControlledCamera")
    public void isCamera(CallbackInfoReturnable<Boolean> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Unique
    private final EventSlowdown eventSlowdown = new EventSlowdown();

    @Redirect(method = {"isSlowDueToUsingItem", "modifyInput"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0))
    private boolean itemUseSlowdownEvent(LocalPlayer self) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Item_Use, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return false;
        }
        return isUsingItem();
    }

    @Redirect(method = "isSprintingPossible", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEnoughFoodToDoExhaustiveManoeuvres()Z"))
    private boolean hungerSlowdownEvent(LocalPlayer self) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Hunger, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return true;
        }
        return self.getFoodData().hasEnoughFood() || self.getAbilities().mayfly;
    }

    @Redirect(method = "isSprintingPossible", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isMobilityRestricted()Z"))
    private boolean onBlindnessSlowdown(LocalPlayer self) {
        eventSlowdown.create(EventSlowdown.SlowdownType.Blindness, 1);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return false;
        }
        return self.isMobilityRestricted();
    }

    @Unique
    private final EventUpdate eventUpdate = new EventUpdate();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null) {
            eventUpdate.create(((LocalPlayer) (Object) this).getX(), ((LocalPlayer) (Object) this).getY(), ((LocalPlayer) (Object) this).getZ(), getYRot(), getXRot(), ((Entity) (Object) this).onGround());
            eventUpdate.broadcast();
            if (eventUpdate.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Override
    public void setHorseJumpPower(float height) {
        jumpRidingScale = height; // TODO: Verify
    }

    @Unique
    private final EventPlayerWalking eventPlayerWalking = new EventPlayerWalking();

    @Inject(method = "sendPosition", at = @At(value = "HEAD"), cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        LocalPlayer entity = (LocalPlayer) (Object) this;
        eventPlayerWalking.create(entity.getX(), entity.getY(), entity.getZ(), getYRot(), getXRot(), ((Entity) (Object) this).onGround());
        eventPlayerWalking.broadcast();
        if (eventPlayerWalking.isCanceled()) {
            ci.cancel();
        }
    }

    @Unique
    private final EventPlayerWalking.PostEvent postEvent = new EventPlayerWalking.PostEvent();

    @Inject(method = "sendPosition", at = @At(value = "TAIL"), cancellable = true)
    private void onSendMovementPacketsTail(CallbackInfo ci) {
        LocalPlayer entity = (LocalPlayer) (Object) this;
        postEvent.create(entity.getX(), entity.getY(), entity.getZ(), getYRot(), getXRot(), ((Entity) (Object) this).onGround());
        postEvent.broadcast();
        if (postEvent.isCanceled()) {
            ci.cancel();
        }
    }

    @Unique
    @Override
    public void message(String text, Class<?> sender) {
        Chat.send(connection::sendChat, text, sender, EventChatSend.Type.Message);
    }

    @Unique
    @Override
    public void command(String text, Class<?> sender) {
        Chat.send(connection::sendCommand, text, sender, EventChatSend.Type.Command);
    }

    @Redirect(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    private double onTickMovement$SlowdownMP(LocalPlayer instance, Holder<Attribute> registryEntry)  {
        double value = instance.getAttributeValue(registryEntry);
        eventSlowdown.create(EventSlowdown.SlowdownType.Sneak, (float) value);
        eventSlowdown.broadcast();
        if (eventSlowdown.isCanceled()) {
            return 1d;
        }
        return value;
    }

}
