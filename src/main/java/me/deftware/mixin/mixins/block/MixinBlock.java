package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.state.IBlockState;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.IBlockReader;
import me.deftware.client.framework.message.Message;
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
    protected boolean blocksMovement;

    @Unique
    private final EventSlowdown eventSlowdown = new EventSlowdown();

    @Inject(method = "getSlipperiness", at = @At("TAIL"), cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (slipperiness != 0.6f) {
            Block block = Block.getBlockFromItem(((Block) (Object) this).asItem());
            boolean flag = false;
            if (block instanceof BlockIce || block.getTranslationKey().contains("blue_ice") || block.getTranslationKey().contains("packed_ice")) {
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

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(IBlockState blockState_1, IBlockReader blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        BlockManagement.shouldDrawSide(blockState_1, blockView_1, blockPos_1, direction_1, callback);
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void getShapeForCollision(IBlockState p_getShapeForCollision_1_, IBlockReader p_getShapeForCollision_2_, BlockPos p_getShapeForCollision_3_, CallbackInfoReturnable<VoxelShape> ci) {
        int id = IRegistry.BLOCK.getId(
                (Block) (Object) this
        );
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.contains(id)) {
            BlockProperty property = blockProperties.get(id);
            if (property.getVoxelShape() != null) {
                ci.setReturnValue((VoxelShape) property.getVoxelShape());
                return;
            }
        }
    }

    @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
    private void getBlockLayer(CallbackInfoReturnable<BlockRenderLayer> cir) {
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = IRegistry.BLOCK.getId(
                    (Block) (Object) this
            );
            if (!blockProperties.contains(id) && blockProperties.isOpacityMode())
                // If the block is not supposed to be rendered then make it transparent
                cir.setReturnValue(BlockRenderLayer.TRANSLUCENT);
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
        return IRegistry.BLOCK.getKey((Block) (Object) this).getPath();
    }

    @Unique
    @Override
    public Item getItem() {
        return (Item) ((Block) (Object) this).asItem();
    }

    @Unique
    @Override
    public int getID() {
        return IRegistry.BLOCK.getId((Block) (Object) this);
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((Block) (Object) this).getNameTextComponent();
    }

}
