package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventSlowdown;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoulSandBlock.class)
public class MixinBlockSoulSand {

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void onEntityCollision(BlockState blockState_1, World world_1, BlockPos blockPos_1, Entity entity_1, CallbackInfo ci) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Soulsand);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

}
