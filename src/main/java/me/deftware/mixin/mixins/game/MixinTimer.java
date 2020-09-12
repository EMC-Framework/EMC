package me.deftware.mixin.mixins.game;

import me.deftware.mixin.imp.IMixinTimer;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Timer.class)
public class MixinTimer implements IMixinTimer {

    @Final
    @Shadow
    private float tickLength;

    private float speed = 1;

    @Redirect(method = "updateTimer", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Timer;tickLength:F", opcode = 180))
    private float getTickLength(Timer self) {
        return tickLength / speed;
    }

    @Override
    public void setTimerSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getTimerSpeed() {
        return speed;
    }

}
