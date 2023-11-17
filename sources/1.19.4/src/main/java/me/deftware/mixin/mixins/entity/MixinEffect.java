package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.message.Message;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StatusEffect.class)
public class MixinEffect implements Effect {

    @Unique
    @Override
    public Type getType() {
        return Effect.Type.values()[((StatusEffect) (Object) this).getCategory().ordinal()];
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((StatusEffect) (Object) this).getName();
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((StatusEffect) (Object) this).getTranslationKey();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return Registries.STATUS_EFFECT.getId((StatusEffect) (Object) this).getPath();
    }

}
