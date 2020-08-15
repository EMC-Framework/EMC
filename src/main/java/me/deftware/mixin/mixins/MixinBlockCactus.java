package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockCactus.class)
public class MixinBlockCactus {

    @Inject(method = "getCollisionBoundingBox", at = @At(value = "TAIL"), cancellable = true)
    private void onGetCollisionShape(World view, BlockPos pos, IBlockState state, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "custom_cactus_voxel", false)) {
            cir.setReturnValue(IAxisAlignedBB.FULL_BLOCK_AABB);
        }
    }
}
