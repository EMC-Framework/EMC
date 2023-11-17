package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.message.Message;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PotionEffect.class)
public class MixinAppliedEffect implements AppliedEffect {

    @Unique
    @Override
    public Effect getEffect() {
        return (Effect) ((PotionEffect) (Object) this).getPotion();
    }

    @Unique
    @Override
    public int getDuration() {
        return ((PotionEffect) (Object) this).getDuration();
    }

    @Unique
    @Override
    public int getAmplifier() {
        return ((PotionEffect) (Object) this).getDuration();
    }

    @Unique
    @Override
    public boolean isAmbient() {
        return ((PotionEffect) (Object) this).getIsAmbient();
    }

    @Unique
    @Override
    public boolean isPermanent() {
        return ((PotionEffect) (Object) this).getIsPotionDurationMax();
    }

    @Unique
    @Override
    public Message getName() {
        Message name = getEffect().getName().copy();
        int amplifier = getAmplifier();
        if (amplifier >= 1 && amplifier <= 9) {
            Message level = Message.translated("enchantment.level." + (amplifier + 1));
            name = name.append(Message.SPACE).append(level);
        }
        return name;
    }

    @Unique
    @Override
    public Message getDurationText() {
        if (isPermanent()) {
            return Message.of("Infinite");
        }
        return Message.of(Potion.getPotionDurationString((PotionEffect) (Object) this, 1.0F));
    }

}
