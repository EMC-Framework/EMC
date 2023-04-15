package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.message.Message;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Potion.class)
public class MixinEffect implements Effect {

    @Unique
    private ResourceLocation resourceLocation;

    @Inject(method = "<init>(ILnet/minecraft/util/ResourceLocation;ZI)V", at = @At("RETURN"))
    private void onCreate(int potionID, ResourceLocation location, boolean badEffect, int potionColor, CallbackInfo ci) {
        this.resourceLocation = location;
    }

    @Unique
    @Override
    public Type getType() {
        if (((Potion) (Object) this).isBadEffect()) {
            return Type.HARMFUL;
        }
        return Type.NEUTRAL;
    }

    @Unique
    @Override
    public Message getName() {
        return Message.translated(((Potion) (Object) this).getName());
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Potion) (Object) this).getName();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return resourceLocation.getResourcePath();
    }

}
