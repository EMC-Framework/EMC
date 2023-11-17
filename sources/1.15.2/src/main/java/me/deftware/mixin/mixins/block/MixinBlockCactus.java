package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CactusBlock.class)
public class MixinBlockCactus {

    @Inject(method = "getCollisionShape", at = @At(value = "TAIL"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockView view, BlockPos pos, EntityContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (GameMap.INSTANCE.get(GameKeys.FULL_CACTUS_VOXEL, false))
            cir.setReturnValue(VoxelShapes.fullCube());
    }
}
