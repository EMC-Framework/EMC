package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventBlockBreakingSpeed;
import me.deftware.client.framework.event.events.EventSneakingCheck;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

    @Shadow @Final private PlayerAbilities abilities;

    @Redirect(method = "adjustMovementForSneaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;clipAtLedge()Z"))
    private boolean sneakingCheck(PlayerEntity self) {
        if (self == MinecraftClient.getInstance().player) {
            EventSneakingCheck event = new EventSneakingCheck(self.isSneaking());
            event.broadcast();
            return event.isSneaking();
        }
        return self.isSneaking();
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At(value = "RETURN"), cancellable = true)
    public void onGetBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        EventBlockBreakingSpeed event = new EventBlockBreakingSpeed().broadcast();
        cir.setReturnValue(cir.getReturnValue() * event.getMultiplier());
    }

    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onGetEntityReachDistance(CallbackInfoReturnable<Double> cir) {
        var map = GameMap.INSTANCE;
        if (map.contains(GameKeys.BLOCK_REACH_DISTANCE)) {
            float value = map.get(GameKeys.BLOCK_REACH_DISTANCE, null);
            cir.setReturnValue((double) value);
        }
    }

}
