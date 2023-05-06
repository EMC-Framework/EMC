package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.global.types.PropertyManager;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.math.Voxel;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import me.deftware.client.framework.message.Message;
import net.minecraft.world.World;
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
    protected BlockState blockState;

    protected boolean blocksMovement;

    /*@Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
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
    }*/

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> cir) {
        PropertyManager<BlockProperty> blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = Block.blockRegistry.getIDForObject((Block) (Object) this);
            if (blockProperties.contains(id))
                cir.setReturnValue(blockProperties.get(id).getLuminance());
        }
    }

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldDrawSide(IBlockAccess blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        BlockManagement.shouldDrawSide(blockState, blockView_1, blockPos_1, direction_1, callback);
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(World p_getShapeForCollision_1_, BlockPos p_getShapeForCollision_2_, IBlockState p_getShapeForCollision_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
        me.deftware.client.framework.world.block.Block block = this;
        EventVoxelShape event = new EventVoxelShape(blocksMovement ? ((Block) (Object) this).getCollisionBoundingBox(p_getShapeForCollision_1_, p_getShapeForCollision_2_, p_getShapeForCollision_3_) : (AxisAlignedBB) Voxel.empty(), block);
        int id = Block.blockRegistry.getIDForObject(
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

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Block) (Object) this).getUnlocalizedName();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return Block.blockRegistry.getNameForObject((Block) (Object) this).getResourcePath();
    }

    @Unique
    @Override
    public Item getItem() {
        return (Item) net.minecraft.item.Item.getItemFromBlock(((Block) (Object) this));
    }

    @Unique
    @Override
    public int getID() {
        return Block.blockRegistry.getIDForObject((Block) (Object) this);
    }

    @Unique
    @Override
    public Message getName() {
        return Message.of(((Block) (Object) this).getLocalizedName());
    }

}
