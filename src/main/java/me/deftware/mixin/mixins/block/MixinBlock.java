package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.mixin.imp.IMixinAbstractBlock;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Shadow
    public abstract Item asItem();

    @Unique
    private final EventSlowdown eventSlowdown = new EventSlowdown();

    @Inject(method = "getSlipperiness", at = @At("TAIL"), cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (((IMixinAbstractBlock) this).getTheSlipperiness() != 0.6f) {
            Block block = Block.getBlockFromItem(this.asItem());
            boolean flag = false;
            if (block instanceof IceBlock || block.getTranslationKey().contains("blue_ice") || block.getTranslationKey().contains("packed_ice")) {
                flag = true;
                eventSlowdown.create(EventSlowdown.SlowdownType.Slipperiness, 0.6f);
            }
            if (flag) {
                eventSlowdown.broadcast();
                if (eventSlowdown.isCanceled()) {
                    cir.setReturnValue(eventSlowdown.getMultiplier());
                }
            }
        }
    }

    @Inject(method = "getVelocityMultiplier", at = @At(value = "TAIL"), cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if (((IMixinAbstractBlock) this).getTheVelocityMultiplier() != 1.0f) {
            Block block = Block.getBlockFromItem(this.asItem());
            boolean flag = false;
            if (block instanceof HoneyBlock) {
                flag = true;
                eventSlowdown.create(EventSlowdown.SlowdownType.Honey, 1);
            } else if (block instanceof SoulSandBlock) {
                flag = true;
                eventSlowdown.create(EventSlowdown.SlowdownType.Soulsand, 1);
            }
            if (flag) {
                eventSlowdown.broadcast();
                if (eventSlowdown.isCanceled()) {
                    cir.setReturnValue(eventSlowdown.getMultiplier());
                }
            }
        }
    }

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction direction, BlockPos blockPos, CallbackInfoReturnable<Boolean> callback) {
        BlockManagement.shouldDrawSide(state, world, pos, direction, callback);
    }

}
