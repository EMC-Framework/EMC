package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.event.events.EventAttackEntity;
import me.deftware.client.framework.event.events.EventBlockBreakingCooldown;
import me.deftware.client.framework.event.events.EventBlockUpdate;
import me.deftware.client.framework.event.events.EventItemUse;
import me.deftware.client.framework.network.packets.CPacketUseEntity;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinPlayerControllerMP;
import me.deftware.mixin.imp.IMixinPlayerInteractEntityC2SPacket;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MixinPlayerControllerMP implements IMixinPlayerControllerMP {

    @Shadow
    private boolean isDestroying;

    @Shadow private int destroyDelay;

    /* TODO
    @Inject(method = "getReachDistance", at = @At(value = "RETURN"), cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(GameMap.INSTANCE.get(GameKeys.BLOCK_REACH_DISTANCE, cir.getReturnValue()));
    }

    @Inject(method = "hasExtendedReach", at = @At(value = "TAIL"), cancellable = true)
    private void onHasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(GameMap.INSTANCE.get(GameKeys.EXTENDED_REACH, cir.getReturnValue()));
    }*/

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attackEntity(Player player, Entity target, CallbackInfo ci) {
        if (target == null || target == player || (CameraEntityMan.isActive() && target == CameraEntityMan.fakePlayer)) {
            ci.cancel();
        } else {
            EventAttackEntity event = new EventAttackEntity(player, target).broadcast();
            if (event.isCanceled())
                ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/EntityHitResult;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", cancellable = true)
    public void interactEntityAtLocation(Player player, Entity entity, EntityHitResult hitResult, InteractionHand hand, CallbackInfoReturnable<InteractionResult> ci) {
        if (entity == null || entity == player) {
            ci.setReturnValue(InteractionResult.FAIL);
            ci.cancel();
        }
    }

    @Redirect(method = "continueDestroyBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I", opcode = 181))
    private void onUpdateBlockBreaking(MultiPlayerGameMode clientPlayerInteractionManager, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value).broadcast();
        destroyDelay = event.getCooldown();
    }

    @Override
    public void setPlayerHittingBlock(boolean state) {
        this.isDestroying = state;
    }

    // Lambda function in interactItem
    @Inject(method = "lambda$useItem$0", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"))
    private void onItemUse(InteractionHand hand, Player player, MutableObject<InteractionResult> mutableObject, int sequence, CallbackInfoReturnable<Packet<?>> cir) {
        var stack = player.getItemInHand(hand);
        var item = stack.getItem();
        new EventItemUse(
                (me.deftware.client.framework.item.Item) item,
                EntityHand.of(hand)
        ).broadcast();
    }

    @Redirect(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState onBlockBreak(Block block, Level world, BlockPos pos, BlockState state, Player player) {
        var result = block.playerWillDestroy(world, pos, state, player);
        new EventBlockUpdate(EventBlockUpdate.State.Break,
                (BlockPosition) pos,
                (me.deftware.client.framework.world.block.Block) block,
                EntityHand.MainHand
        ).broadcast();
        return result;
    }

    @Redirect(method = "performUseItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"))
    private InteractionResult onBlockPlace(ItemStack instance, UseOnContext context) {
        Item item = instance.getItem();
        InteractionResult result = instance.useOn(context);

        if (result.consumesAction()) {
            if (item instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

                new EventBlockUpdate(
                        EventBlockUpdate.State.Place,
                        (BlockPosition) pos,
                        (me.deftware.client.framework.world.block.Block) block,
                        EntityHand.of(context.getHand())
                ).broadcast();
            } else {
                new EventItemUse(
                        (me.deftware.client.framework.item.Item) item,
                        EntityHand.of(context.getHand())
                ).broadcast();
            }
        }
        return result;
    }

}
