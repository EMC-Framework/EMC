package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.imp.IMixinAbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Shadow
    public abstract Item asItem();

    @Inject(method = "getSlipperiness", at = @At("TAIL"), cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (((IMixinAbstractBlock) this).getTheSlipperiness() != 0.6f) {
            Block block = Block.getBlockFromItem(this.asItem());
            EventSlowdown event = null;
            if (block instanceof IceBlock || block.getTranslationKey().contains("blue_ice") || block.getTranslationKey().contains("packed_ice")) {
                event = new EventSlowdown(EventSlowdown.SlowdownType.Slipperiness, 0.6f);
            }
            if (event != null) {
                event.broadcast();
                if (event.isCanceled()) {
                    cir.setReturnValue(event.getMultiplier());
                }
            }
        }
    }

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> callback) {
        BlockManagement.shouldDrawSide(state, world, pos, facing, callback);
    }

    @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
    private void getBlockLayer(CallbackInfoReturnable<RenderLayer> cir) {
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = Registry.BLOCK.getRawId(
                    (Block) (Object) this
            );
            if (!blockProperties.contains(id) && blockProperties.isOpacityMode())
                // If the block is not supposed to be rendered then make it transparent
                cir.setReturnValue(RenderLayer.TRANSLUCENT);
        }
    }

}
