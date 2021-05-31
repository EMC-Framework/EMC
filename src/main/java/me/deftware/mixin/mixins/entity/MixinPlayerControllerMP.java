package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventAttackEntity;
import me.deftware.client.framework.event.events.EventBlockBreakingCooldown;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinPlayerControllerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP implements IMixinPlayerControllerMP {

    @Shadow
    private boolean isHittingBlock;

    @Shadow private int blockHitDelay;

    @Inject(method = "getBlockReachDistance", at = @At(value = "RETURN"), cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(GameMap.INSTANCE.get(GameKeys.BLOCK_REACH_DISTANCE, cir.getReturnValue()));
    }


    @Inject(method = "extendedReach", at = @At(value = "TAIL"), cancellable = true)
    private void onHasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(GameMap.INSTANCE.get(GameKeys.EXTENDED_REACH, cir.getReturnValue()));
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    public void attackEntity(EntityPlayer player, Entity target, CallbackInfo ci) {
        if (target == null || target == player || (CameraEntityMan.isActive() && target == CameraEntityMan.fakePlayer)) {
            ci.cancel();
        } else {
            EventAttackEntity event = new EventAttackEntity(player, target);
            event.broadcast();
        }
    }

    /*@Inject(at = @At("HEAD"), method = "interactWithEntitySendPacket(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/EnumActionResult;", cancellable = true) FIXME
    private void interactEntity(EntityPlayer player, Entity target, ItemStack stack, EnumHand hand, CallbackInfoReturnable<EnumActionResult> info) {
        if (target == null || target == player) {
            info.setReturnValue(EnumActionResult.FAIL);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "interactWithEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/RayTraceResult;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/EnumActionResult;", cancellable = true)
    public void interactEntityAtLocation(EntityPlayer player, Entity entity, RayTraceResult hitResult, ItemStack stack, EnumHand hand, CallbackInfoReturnable<EnumActionResult> ci) {
        if (entity == null || entity == player) {
            ci.setReturnValue(EnumActionResult.FAIL);
            ci.cancel();
        }
    }*/

    @Redirect(method = "onPlayerDamageBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;blockHitDelay:I", opcode = 181))
    private void onUpdateBlockBreaking(PlayerControllerMP clientPlayerInteractionManager, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value).broadcast();
        blockHitDelay = event.getCooldown();
    }

    @Override
    public void setPlayerHittingBlock(boolean state) {
        this.isHittingBlock = state;
    }

}
