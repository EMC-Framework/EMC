package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CactusBlock.class)
public class MixinBlockCactus {

    @Inject(method = "getCollisionShape", at = @At(value = "TAIL"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (GameMap.INSTANCE.get(GameKeys.FULL_CACTUS_VOXEL, false))
            cir.setReturnValue(Shapes.block());
    }
}
