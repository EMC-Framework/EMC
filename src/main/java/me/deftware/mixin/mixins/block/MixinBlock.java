package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.mixin.imp.IMixinAbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    protected boolean blocksMovement;

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
                ci.setReturnValue(property.getVoxelShape().getMinecraftVoxelShape());
                return;
            }
        }
        // Deprecated
        EventVoxelShape event = new EventVoxelShape(blocksMovement ? p_getShapeForCollision_1_.getCollisionBoundingBox(p_getShapeForCollision_2_, p_getShapeForCollision_3_) : Block.NULL_AABB, me.deftware.client.framework.world.block.Block.newInstance((Block) (Object) this));
        event.broadcast();
        if (event.modified)
            ci.setReturnValue(event.shape);
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

}
