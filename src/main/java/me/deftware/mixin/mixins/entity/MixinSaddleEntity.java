package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventSaddleCheck;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MixinSaddleEntity {

    @Unique
    private boolean shouldAffectSaddle() {
        var entity = (MobEntity) (Object) this;
        return entity instanceof AbstractHorseEntity ||
                entity instanceof PigEntity ||
                entity instanceof StriderEntity;
    }

    @Inject(method = "hasSaddleEquipped", at = @At(value = "TAIL"), cancellable = true)
    private void onIsSaddled(CallbackInfoReturnable<Boolean> cir) {
        if (shouldAffectSaddle()) {
            EventSaddleCheck event = new EventSaddleCheck(cir.getReturnValue());
            event.broadcast();
            cir.setReturnValue(event.isState());
        }
    }

}
