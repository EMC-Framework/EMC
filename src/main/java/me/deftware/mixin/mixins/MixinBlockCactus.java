package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockCactus.class)
public class MixinBlockCactus {

    @Inject(method = "getCollisionShape", at = @At(value = "TAIL"), cancellable = true)
    private void onGetCollisionShape(IBlockState state, IBlockReader view, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "custom_cactus_voxel", false)) {
            cir.setReturnValue(ShapeUtils.fullCube());
        }
    }
}
