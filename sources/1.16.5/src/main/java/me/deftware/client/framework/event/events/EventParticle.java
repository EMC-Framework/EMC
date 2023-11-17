package me.deftware.client.framework.event.events;

import com.google.common.collect.Lists;
import me.deftware.client.framework.event.Event;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;

import java.util.List;

public class EventParticle extends Event {

    public static final List<ParticleEffect> IGNORED_PARTICLES = Lists.newArrayList(ParticleTypes.FIREWORK, ParticleTypes.FLASH);

    private String id;
    private double x, y, z, velocityX, velocityZ, velocityY;

    public EventParticle(String id, double x, double y, double z, double velocityX, double velocityZ, double velocityY) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.velocityX = velocityX;
        this.velocityZ = velocityZ;
        this.velocityY = velocityY;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityZ() {
        return velocityZ;
    }

    public double getVelocityY() {
        return velocityY;
    }

}
