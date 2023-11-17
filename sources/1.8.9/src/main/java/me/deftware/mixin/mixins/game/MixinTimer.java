package me.deftware.mixin.mixins.game;

import net.minecraft.util.Timer;
import me.deftware.client.framework.world.WorldTimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Timer.class)
public class MixinTimer implements WorldTimer {

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
