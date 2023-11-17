package me.deftware.mixin.mixins.block;

import net.minecraft.block.BlockCactus;
import net.minecraft.block.state.IBlockState;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockCactus.class)
public class MixinBlockCactus {

    @Inject(method = "getCollisionShape", at = @At(value = "TAIL"), cancellable = true)
    private void onGetCollisionShape(IBlockState state, IBlockReader view, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        if (GameMap.INSTANCE.get(GameKeys.FULL_CACTUS_VOXEL, false))
            cir.setReturnValue(VoxelShapes.fullCube());
    }

}
