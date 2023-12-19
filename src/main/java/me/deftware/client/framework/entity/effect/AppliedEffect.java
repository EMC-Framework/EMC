package me.deftware.client.framework.entity.effect;

import me.deftware.client.framework.message.Message;
import net.minecraft.entity.effect.StatusEffectInstance;

public interface AppliedEffect {

    Effect getEffect();

    int getDuration();

    int getAmplifier();

    boolean isAmbient();

    boolean isPermanent();

    Message getName();

    Message getDurationText();

    static AppliedEffect of(Effect effect, int duration, int amplifier) {
        return of(effect, duration, amplifier, false, true, true);
    }

    static AppliedEffect of(Effect effect, int duration, int amplifier, boolean ambient, boolean visible, boolean icon) {

        return (AppliedEffect) new StatusEffectInstance(
                effect.getStatusEffect(), duration, amplifier, ambient, visible, icon
        );
    }

}
