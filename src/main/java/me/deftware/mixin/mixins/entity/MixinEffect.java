package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.message.Message;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MobEffect.class)
public class MixinEffect implements Effect {

    @Unique
    @Override
    public Type getType() {
        return Effect.Type.values()[((MobEffect) (Object) this).getCategory().ordinal()];
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((MobEffect) (Object) this).getDisplayName();
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((MobEffect) (Object) this).getDescriptionId();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return BuiltInRegistries.MOB_EFFECT.getKey((MobEffect) (Object) this).getPath();
    }

}
