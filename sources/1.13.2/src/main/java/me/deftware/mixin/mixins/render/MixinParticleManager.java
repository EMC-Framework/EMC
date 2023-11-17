package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.IParticleData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class MixinParticleManager {

    @Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(IParticleData parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> ci) {
        EventParticle event = new EventParticle(parameters.getParameters(), x, y, z, velocityX, velocityY, velocityZ).broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

}
