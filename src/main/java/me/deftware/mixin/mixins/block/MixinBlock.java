package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.math.box.VoxelShape;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.mixin.imp.IMixinAbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Shadow
    @Final
    protected BlockState blockState;

    protected boolean blocksMovement;

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldDrawSide(IBlockAccess blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        BlockManagement.shouldDrawSide(blockState, blockView_1, blockPos_1, direction_1, callback);
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(World p_getShapeForCollision_1_, BlockPos p_getShapeForCollision_2_, IBlockState p_getShapeForCollision_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
        me.deftware.client.framework.world.block.Block block = me.deftware.client.framework.world.block.Block.newInstance((Block) (Object) this);
        EventVoxelShape event = new EventVoxelShape(blocksMovement ? block.getMinecraftBlock().getCollisionBoundingBox(p_getShapeForCollision_1_, p_getShapeForCollision_2_, p_getShapeForCollision_3_) : VoxelShape.EMPTY.getMinecraftVoxelShape(), block);
        event.broadcast();
        if (event.modified)
            ci.setReturnValue(event.shape);
    }

    @Inject(method = "getBlockLayer", at = @At("HEAD"), cancellable = true)
    private void getBlockLayer(CallbackInfoReturnable<EnumWorldBlockLayer> cir) {
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = Block.blockRegistry.getIDForObject(
                    (Block) (Object) this
            );
            if (!blockProperties.contains(id) && blockProperties.isOpacityMode())
                // If the block is not supposed to be rendered then make it transparent
                cir.setReturnValue(EnumWorldBlockLayer.TRANSLUCENT);
        }
    }


}
