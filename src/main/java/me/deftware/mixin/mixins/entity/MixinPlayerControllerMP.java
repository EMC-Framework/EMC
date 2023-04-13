package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.event.events.EventAttackEntity;
import me.deftware.client.framework.event.events.EventBlockBreakingCooldown;
import me.deftware.client.framework.event.events.EventBlockUpdate;
import me.deftware.client.framework.event.events.EventItemUse;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.registry.ItemRegistry;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinPlayerControllerMP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    @Inject(at = @At("HEAD"), method = "interactWithEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/EnumActionResult;", cancellable = true)
    private void interactEntity(EntityPlayer player, Entity target, EnumHand hand, CallbackInfoReturnable<EnumActionResult> info) {
        if (target == null || target == player) {
            info.setReturnValue(EnumActionResult.FAIL);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "interactWithEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/RayTraceResult;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/EnumActionResult;", cancellable = true)
    public void interactEntityAtLocation(EntityPlayer player, Entity entity, RayTraceResult hitResult, EnumHand hand, CallbackInfoReturnable<EnumActionResult> ci) {
        if (entity == null || entity == player) {
            ci.setReturnValue(EnumActionResult.FAIL);
            ci.cancel();
        }
    }

    @Redirect(method = "onPlayerDamageBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;blockHitDelay:I", opcode = 181))
    private void onUpdateBlockBreaking(PlayerControllerMP clientPlayerInteractionManager, int value) {
        EventBlockBreakingCooldown event = new EventBlockBreakingCooldown(value).broadcast();
        blockHitDelay = event.getCooldown();
    }

    @Override
    public void setPlayerHittingBlock(boolean state) {
        this.isHittingBlock = state;
    }

    @Redirect(method = "processRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;"))
    private ActionResult<ItemStack> onItemUse(ItemStack instance, World world, EntityPlayer user, EnumHand hand) {
        Item item = instance.getItem();
        ActionResult<ItemStack> result = instance.useItemRightClick(world, user, hand);

        new EventItemUse(
                ItemRegistry.INSTANCE.getItem(item),
                EntityHand.of(hand)
        ).broadcast();

        return result;
    }

    @Redirect(method = "onPlayerDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onPlayerDestroy(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V"))
    private void onBlockBreak(Block block, World world, BlockPos pos, IBlockState state) {
        block.onPlayerDestroy(world, pos, state);
        new EventBlockUpdate(EventBlockUpdate.State.Break,
                (BlockPosition) pos,
                BlockRegistry.INSTANCE.getBlock(block),
                EntityHand.MainHand
        ).broadcast();
    }

    @Redirect(method = "processRightClickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onItemUse(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;"))
    private EnumActionResult onBlockPlace(ItemStack instance, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Item item = instance.getItem();
        EnumActionResult result = instance.onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);

        if ((result == EnumActionResult.SUCCESS || result == EnumActionResult.PASS) && item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            BlockPos offset = pos.offset(side);

            new EventBlockUpdate(
                    EventBlockUpdate.State.Place,
                    (BlockPosition) offset,
                    BlockRegistry.INSTANCE.getBlock(block),
                    EntityHand.MainHand
            ).broadcast();
        }
        return result;
    }

}
