package me.deftware.mixin.mixins.game;

import me.deftware.client.framework.world.WorldTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.TickRateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MixinTimer implements WorldTimer {

    @Unique
    private float speed = 1;

    @ModifyVariable(method = "getTickTargetMillis", at = @At("HEAD"), argsOnly = true)
    private float onGetTargetMillis(float millis) {
        return millis / speed;
    }

    @Redirect(method = "getTickTargetMillis", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/TickRateManager;millisecondsPerTick()F"))
    private float onMax(TickRateManager instance) {
        return instance.millisecondsPerTick() / speed;
    }

    @Override
    public float getTimerSpeed() {
        return speed;
    }

    @Override
    public void setTimerSpeed(float speed) {
        this.speed = speed;
    }

}
