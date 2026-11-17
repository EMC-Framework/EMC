package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventSaddleCheck;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MixinSaddleEntity {

    @Unique
    private boolean shouldAffectSaddle() {
        var entity = (Mob) (Object) this;
        return entity instanceof AbstractHorse ||
                entity instanceof Pig ||
                entity instanceof Strider;
    }

    @Inject(method = "isSaddled", at = @At(value = "TAIL"), cancellable = true)
    private void onIsSaddled(CallbackInfoReturnable<Boolean> cir) {
        if (shouldAffectSaddle()) {
            EventSaddleCheck event = new EventSaddleCheck(cir.getReturnValue());
            event.broadcast();
            cir.setReturnValue(event.isState());
        }
    }

}
