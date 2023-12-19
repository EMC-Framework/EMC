package me.deftware.client.framework.entity.effect;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.registry.Identifiable;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

public interface Effect extends Identifiable {

    Type getType();

    Message getName();

    enum Type {
        BENEFICIAL,
        HARMFUL,
        NEUTRAL
    }

    default RegistryEntry<StatusEffect> getStatusEffect() {
        return Registries.STATUS_EFFECT.getEntry((StatusEffect) this);
    }

}
