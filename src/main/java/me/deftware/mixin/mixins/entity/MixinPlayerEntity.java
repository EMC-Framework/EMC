package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventBlockBreakingSpeed;
import me.deftware.client.framework.event.events.EventSneakingCheck;
import me.deftware.client.framework.event.events.EventSpectator;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayerEntity {

    @Shadow @Final private Abilities abilities;

    @Redirect(method = "maybeBackOffFromEdge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isStayingOnGroundSurface()Z"))
    private boolean sneakingCheck(Player self) {
        if (self == Minecraft.getInstance().player) {
            EventSneakingCheck event = new EventSneakingCheck(self.isShiftKeyDown());
            event.broadcast();
            return event.isSneaking();
        }
        return self.isShiftKeyDown();
    }

    @Inject(method = "getDestroySpeed", at = @At(value = "RETURN"), cancellable = true)
    public void onGetBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        EventBlockBreakingSpeed event = new EventBlockBreakingSpeed().broadcast();
        cir.setReturnValue(cir.getReturnValue() * event.getMultiplier());
    }

    @Inject(method = "blockInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onGetBlockReachDistance(CallbackInfoReturnable<Double> cir) {
        var map = GameMap.INSTANCE;
        if (map.contains(GameKeys.BLOCK_REACH_DISTANCE)) {
            Float value = map.get(GameKeys.BLOCK_REACH_DISTANCE, null);
            if (value != null) {
                cir.setReturnValue((double) value);
            }
        }
    }

    @Inject(method = "entityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onGetEntityReachDistance(CallbackInfoReturnable<Double> cir) {
        var map = GameMap.INSTANCE;
        if (map.contains(GameKeys.BLOCK_REACH_DISTANCE)) {
            Float value = map.get(GameKeys.BLOCK_REACH_DISTANCE, null);
            if (value != null) {
                cir.setReturnValue((double) value);
            }
        }
    }

    @Inject(method = "isSpectator", at = @At("RETURN"), cancellable = true)
    private void onIsSpectator(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractClientPlayer) {
            EventSpectator event = new EventSpectator(cir.getReturnValue());
            event.broadcast();
            cir.setReturnValue(event.isSpectator());
        }
    }

}
