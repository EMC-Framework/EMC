package me.deftware.mixin.mixins.render;

import com.google.common.collect.Lists;
import me.deftware.client.framework.event.events.EventParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {

    @Unique
    private final List<ParticleEffect> IGNORED_PARTICLES = Lists.newArrayList(ParticleTypes.FIREWORK, ParticleTypes.FLASH);

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> ci) {
        if (!IGNORED_PARTICLES.contains(parameters)) {
            EventParticle event = new EventParticle(parameters.asString(), x, y, z, velocityX, velocityY, velocityZ).broadcast();
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

}
