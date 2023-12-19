package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventIsPotionActive;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public class MixinEntityLivingBase implements IMixinEntityLivingBase {

    @Shadow
    @Final
    private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;

    @Shadow
    protected int itemUseTimeLeft;

    @Inject(method = "hasStatusEffect", at = @At(value = "TAIL"), cancellable = true)
    private void onHasStatusEffect(RegistryEntry<StatusEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        if (((LivingEntity) (Object) this).isPlayer()) {
            EventIsPotionActive event = new EventIsPotionActive(effect.value().getTranslationKey(), activeStatusEffects.containsKey(effect)).broadcast();
            cir.setReturnValue(event.isActive());
        }
    }

    @Inject(method = "getJumpVelocity", at = @At(value = "TAIL"), cancellable = true)
    private void onGetJumpVelocity(CallbackInfoReturnable<Float> cir) {
        if (((LivingEntity) (Object) this).isPlayer())
            cir.setReturnValue(GameMap.INSTANCE.get(GameKeys.JUMP_HEIGHT, cir.getReturnValue()));
    }

    @Override
    public int getActiveItemStackUseCount() {
        return itemUseTimeLeft;
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
    private boolean travelHasStatusEffectProxy(LivingEntity self, RegistryEntry<StatusEffect> statusEffect) {
        if (statusEffect == StatusEffects.LEVITATION && !GameMap.INSTANCE.get(GameKeys.LEVITATION, true) && ((LivingEntity) (Object) this).isPlayer())
            return false;
        return self.hasStatusEffect(statusEffect);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasNoGravity()Z"))
    private boolean travelHasNoGravityProxy(LivingEntity self) {
        if (self.hasStatusEffect(StatusEffects.LEVITATION) && !GameMap.INSTANCE.get(GameKeys.LEVITATION, true) && ((LivingEntity) (Object) this).isPlayer())
            return false;
        return self.hasNoGravity();
    }

    @Unique
    private float height = -1;

    @Unique
    @Override
    public void _setStepHeight(float height) {
        this.height = height;
    }

    @Redirect(method = "getStepHeight", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"))
    private double onGetHeight(LivingEntity instance, RegistryEntry<EntityAttribute> attribute) {
        if (height > 0) {
            return height;
        }
        return instance.getAttributeValue(attribute);
    }

}
