package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.message.Message;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StatusEffectInstance.class)
public class MixinAppliedEffect implements AppliedEffect {

    @Unique
    @Override
    public Effect getEffect() {
        return (Effect) ((StatusEffectInstance) (Object) this).getEffectType().value();
    }

    @Unique
    @Override
    public int getDuration() {
        return ((StatusEffectInstance) (Object) this).getDuration();
    }

    @Unique
    @Override
    public int getAmplifier() {
        return ((StatusEffectInstance) (Object) this).getDuration();
    }

    @Unique
    @Override
    public boolean isAmbient() {
        return ((StatusEffectInstance) (Object) this).isAmbient();
    }

    @Unique
    @Override
    public boolean isPermanent() {
        return ((StatusEffectInstance) (Object) this).isInfinite();
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
        MinecraftClient mc = MinecraftClient.getInstance();
        float tickRate = 20f;
        if (mc.world != null) {
            tickRate = mc.world.getTickManager().getTickRate();;
        }
        return (Message) StatusEffectUtil.getDurationText((StatusEffectInstance) (Object) this, 1.0F, tickRate);
    }

}
