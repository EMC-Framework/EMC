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

    @Shadow
    public float timerSpeed = 1.0F;

    @Override
    public void setTimerSpeed(float speed) {
        this.timerSpeed = speed;
    }

    @Override
    public float getTimerSpeed() {
        return timerSpeed;
    }

}
