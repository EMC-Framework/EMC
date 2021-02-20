package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.math.box.VoxelShape;
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
        if (GameMap.INSTANCE.get(GameKeys.FULL_CACTUS_VOXEL, false))
            cir.setReturnValue(VoxelShape.SOLID.getMinecraftVoxelShape());
    }

}
