package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.message.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MobEffectInstance.class)
public class MixinAppliedEffect implements AppliedEffect {

    @Unique
    @Override
    public Effect getEffect() {
        return (Effect) ((MobEffectInstance) (Object) this).getEffect().value();
    }

    @Unique
    @Override
    public int getDuration() {
        return ((MobEffectInstance) (Object) this).getDuration();
    }

    @Unique
    @Override
    public int getAmplifier() {
        return ((MobEffectInstance) (Object) this).getAmplifier();
    }

    @Unique
    @Override
    public boolean isAmbient() {
        return ((MobEffectInstance) (Object) this).isAmbient();
    }

    @Unique
    @Override
    public boolean isPermanent() {
        return ((MobEffectInstance) (Object) this).isInfiniteDuration();
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
        Minecraft mc = Minecraft.getInstance();
        float tickRate = 20f;
        if (mc.level != null) {
            tickRate = mc.level.tickRateManager().tickrate();;
        }
        return (Message) MobEffectUtil.formatDuration((MobEffectInstance) (Object) this, 1.0F, tickRate);
    }

}
