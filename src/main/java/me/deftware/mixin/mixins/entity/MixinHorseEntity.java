package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventSaddleCheck;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public class MixinHorseEntity {

    @Inject(method = "canUseSlot", at = @At("TAIL"), cancellable = true)
    public void canBeControlled(EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        if (slot == EquipmentSlot.SADDLE) {
            EventSaddleCheck event = new EventSaddleCheck(cir.getReturnValue());
            event.broadcast();
            cir.setReturnValue(event.isState());
        }
    }

}
