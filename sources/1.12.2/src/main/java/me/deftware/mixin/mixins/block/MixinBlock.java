package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.global.types.PropertyManager;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.math.BlockPosition;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import me.deftware.client.framework.message.Message;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock implements me.deftware.client.framework.world.block.Block {

    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<AxisAlignedBB> ci) {
        EventCollideCheck event = new EventCollideCheck(
                (me.deftware.client.framework.world.block.Block) blockState_1.getBlock(),
                (BlockPosition) blockPos_1
        ).broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(Block.NULL_AABB);
            }
        }
    }

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(IBlockState blockState_1, CallbackInfoReturnable<Integer> callback) {
        PropertyManager<BlockProperty> blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = Block.REGISTRY.getIDForObject(blockState_1.getBlock());
            if (blockProperties.contains(id))
                callback.setReturnValue(blockProperties.get(id).getLuminance());
        }
    }


    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldDrawSide(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        BlockManagement.shouldDrawSide(blockState_1, blockView_1, blockPos_1, direction_1, callback);
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(IBlockState p_getShapeForCollision_1_, IBlockAccess p_getShapeForCollision_2_, BlockPos p_getShapeForCollision_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
        int id = Block.REGISTRY.getIDForObject(
                (Block) (Object) this
        );
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.contains(id)) {
            BlockProperty property = blockProperties.get(id);
            if (property.getVoxelShape() != null) {
                ci.setReturnValue((AxisAlignedBB) property.getVoxelShape());
            }
        }
    }

    @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
    private void getBlockLayer(CallbackInfoReturnable<BlockRenderLayer> cir) {
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = Block.REGISTRY.getIDForObject(
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
        return ((Block) (Object) this).getTranslationKey();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return Block.REGISTRY.getNameForObject((Block) (Object) this).getPath();
    }

    @Unique
    @Override
    public Item getItem() {
        return (Item) net.minecraft.item.Item.getItemFromBlock(((Block) (Object) this));
    }

    @Unique
    @Override
    public int getID() {
        return Block.REGISTRY.getIDForObject((Block) (Object) this);
    }

    @Unique
    @Override
    public Message getName() {
        return Message.of(((Block) (Object) this).getLocalizedName());
    }

}
