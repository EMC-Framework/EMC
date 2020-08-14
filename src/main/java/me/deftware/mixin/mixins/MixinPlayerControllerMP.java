package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventAttackEntity;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.mixin.imp.IMixinPlayerControllerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP implements IMixinPlayerControllerMP {

    @Shadow
    private boolean isHittingBlock;

    @Inject(method = "getBlockReachDistance", at = @At(value = "RETURN"), cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "BLOCK_REACH_DISTANCE", cir.getReturnValue()));
    }


    @Inject(method = "extendedReach", at = @At(value = "TAIL"), cancellable = true)
    private void onHasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "EXTENDED_REACH", cir.getReturnValue()));
    }

    @Inject(method = "attackEntity", at = @At("HEAD"))
    public void attackEntity(EntityPlayer player, Entity target, CallbackInfo ci) {
        EventAttackEntity event = new EventAttackEntity(player, target);
        event.broadcast();
    }

    @Override
    public void setPlayerHittingBlock(boolean state) {
        this.isHittingBlock = state;
    }

}
