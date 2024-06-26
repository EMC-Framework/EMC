package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ParticleManager.class)
public class MixinParticleManager {

    @Unique
    private final List<ParticleEffect> IGNORED_PARTICLES = List.of(ParticleTypes.FIREWORK, ParticleTypes.FLASH);

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> ci) {
        if (!IGNORED_PARTICLES.contains(parameters)) {
            var entry = Registries.PARTICLE_TYPE.getEntry(parameters.getType());
            var id = entry.getIdAsString();
            EventParticle event = new EventParticle(id, x, y, z, velocityX, velocityY, velocityZ).broadcast();
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

}
