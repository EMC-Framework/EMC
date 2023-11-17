package me.deftware.client.framework.entity.effect;

import me.deftware.client.framework.message.Message;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

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
        return (AppliedEffect) new PotionEffect(
                (Potion) effect, duration, amplifier, ambient, visible
        );
    }

}
