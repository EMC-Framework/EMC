package me.deftware.mixin.mixins.game;

import me.deftware.client.framework.world.WorldTimer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.tick.TickManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MixinTimer implements WorldTimer {

    @Unique
    private float speed = 1;

    @ModifyVariable(method = "getTargetMillisPerTick", at = @At("HEAD"), argsOnly = true)
    private float onGetTargetMillis(float millis) {
        return millis / speed;
    }

    @Redirect(method = "getTargetMillisPerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/TickManager;getMillisPerTick()F"))
    private float onMax(TickManager instance) {
        return instance.getMillisPerTick() / speed;
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
