package me.deftware.mixin.mixins.entity;

import net.minecraft.block.Block;
import me.deftware.client.framework.event.events.EventBlockBreakingSpeed;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class MixinPlayerEntity {

    @Inject(method = "getToolDigEfficiency", at = @At(value = "RETURN"), cancellable = true)
    public void onGetBlockBreakingSpeed(Block p_180471_1_, CallbackInfoReturnable<Float> cir) {
        EventBlockBreakingSpeed event = new EventBlockBreakingSpeed().broadcast();
        cir.setReturnValue(cir.getReturnValue() * event.getMultiplier());
    }

}
