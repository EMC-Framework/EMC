package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ParticleEngine.class)
public class MixinParticleManager {

    @Unique
    private static final List<net.minecraft.core.particles.ParticleType<?>> IGNORED_PARTICLES = List.of(ParticleTypes.FIREWORK, ParticleTypes.FLASH);

    @Inject(method = "createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> ci) {
        if (!IGNORED_PARTICLES.contains(parameters.getType())) {
            var entry = BuiltInRegistries.PARTICLE_TYPE.wrapAsHolder(parameters.getType());
            var id = entry.getRegisteredName();
            EventParticle event = new EventParticle(id, x, y, z, velocityX, velocityY, velocityZ).broadcast();
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

}
