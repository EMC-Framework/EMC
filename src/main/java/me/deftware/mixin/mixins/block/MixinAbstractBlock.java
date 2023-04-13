package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.client.framework.global.types.PropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.mixin.imp.IMixinAbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinAbstractBlock implements IMixinAbstractBlock {

    @Shadow @Final
    private float slipperiness;

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockState blockState_1, IBlockReader blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<VoxelShape> ci) {
        EventCollideCheck event = new EventCollideCheck(
                me.deftware.client.framework.world.block.Block.newInstance(blockState_1.getBlock()),
                (BlockPosition) blockPos_1
        ).broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(VoxelShapes.empty());
            }
        }
    }

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(IBlockState blockState_1, CallbackInfoReturnable<Integer> callback) {
        PropertyManager<BlockProperty> blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = IRegistry.BLOCK.getId(blockState_1.getBlock());
            if (blockProperties.contains(id))
                callback.setReturnValue(blockProperties.get(id).getLuminance());
        }
    }

    @Override
    public float getTheSlipperiness() {
        return this.slipperiness;
    }

}
