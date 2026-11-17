package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventDamage;
import me.deftware.client.framework.event.events.EventSlowdown;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SweetBerryBushBlock.class)
public class MixinSweetBerryBushBlock {

    @Unique
    private final EventSlowdown slowdown = new EventSlowdown();

    @Redirect(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;makeStuckInBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/Vec3;)V"))
    private void onSlowMovement(Entity entity, BlockState state, Vec3 multiplier) {
        slowdown.create(EventSlowdown.SlowdownType.BerryBush, 1);
        slowdown.broadcast();
        if(!slowdown.isCanceled()) {
            entity.makeStuckInBlock(state, multiplier);
        }
    }

    @Inject(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"), cancellable = true)
    private void onDamage(BlockState state, Level world, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean b, CallbackInfo ci) {
        EventDamage eventDamage = new EventDamage(EventDamage.DamageSource.BerryBush);
        eventDamage.broadcast();
        if(eventDamage.isCanceled()) {
            ci.cancel();
        }
    }
}
