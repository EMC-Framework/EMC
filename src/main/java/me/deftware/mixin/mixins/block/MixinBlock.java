package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.client.render.RenderLayer;
import me.deftware.client.framework.message.Message;
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
