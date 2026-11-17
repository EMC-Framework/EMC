package me.deftware.client.framework.entity.effect;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.registry.Identifiable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public interface Effect extends Identifiable {

    Type getType();

    Message getName();

    enum Type {
        BENEFICIAL,
        HARMFUL,
        NEUTRAL
    }

    default Holder<MobEffect> getStatusEffect() {
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder((MobEffect) this);
    }

}
