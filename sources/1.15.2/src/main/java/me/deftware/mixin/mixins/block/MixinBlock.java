package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.message.Message;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock implements me.deftware.client.framework.world.block.Block {

    @Shadow
    @Final
    private float slipperiness;

    @Shadow
    @Final
    private float velocityMultiplier;

    @Unique
    private final EventSlowdown eventSlowdown = new EventSlowdown();

    @Inject(method = "getSlipperiness", at = @At("TAIL"), cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (slipperiness != 0.6f) {
            Block block = Block.getBlockFromItem(((Block) (Object) this).asItem());
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
        if (velocityMultiplier != 1.0f) {
            Block block = Block.getBlockFromItem(((Block) (Object) this).asItem());
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
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> callback) {
        BlockManagement.shouldDrawSide(state, world, pos, facing, callback);
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Block) (Object) this).asItem().getTranslationKey();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return Registry.BLOCK.getId((Block) (Object) this).getPath();
    }

    @Unique
    @Override
    public Item getItem() {
        return (Item) ((Block) (Object) this).asItem();
    }

    @Unique
    @Override
    public int getID() {
        return Registry.BLOCK.getRawId((Block) (Object) this);
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((Block) (Object) this).getName();
    }

}
