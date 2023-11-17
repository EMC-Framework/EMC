package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.message.Message;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.IRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Potion.class)
public class MixinEffect implements Effect {

    @Unique
    @Override
    public Type getType() {
        if (((Potion) (Object) this).isBadEffect()) {
            return Type.HARMFUL;
        } else if (((Potion) (Object) this).isBeneficial()) {
            return Type.BENEFICIAL;
        }
        return Type.NEUTRAL;
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((Potion) (Object) this).getDisplayName();
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Potion) (Object) this).getName();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return IRegistry.MOB_EFFECT.getKey((Potion) (Object) this).getPath();
    }

}
