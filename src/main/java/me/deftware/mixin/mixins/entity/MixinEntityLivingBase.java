package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventIsPotionActive;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class MixinEntityLivingBase implements IMixinEntityLivingBase {

    @Shadow
    @Final
    private Map<MobEffect, MobEffectInstance> activeEffects;

    @Shadow
    protected int useItemRemaining;

    @Shadow
    protected abstract double getEffectiveGravity();

    @Inject(method = "hasEffect", at = @At(value = "TAIL"), cancellable = true)
    private void onHasStatusEffect(Holder<MobEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        if (((LivingEntity) (Object) this).isAlwaysTicking()) {
            EventIsPotionActive event = new EventIsPotionActive(effect.value().getDescriptionId(), activeEffects.containsKey(effect)).broadcast();
            cir.setReturnValue(event.isActive());
        }
    }

    @Inject(method = "getJumpPower()F", at = @At(value = "TAIL"), cancellable = true)
    private void onGetJumpVelocity(CallbackInfoReturnable<Float> cir) {
        if (((LivingEntity) (Object) this).isAlwaysTicking())
            cir.setReturnValue(GameMap.INSTANCE.get(GameKeys.JUMP_HEIGHT, cir.getReturnValue()));
    }

    @Override
    public int getActiveItemStackUseCount() {
        return useItemRemaining;
    }

    @Redirect(method = "travelInAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getEffect(Lnet/minecraft/core/Holder;)Lnet/minecraft/world/effect/MobEffectInstance;"))
    private MobEffectInstance travelHasStatusEffectProxy(LivingEntity self, Holder<MobEffect> statusEffect) {
        if (statusEffect == MobEffects.LEVITATION && !GameMap.INSTANCE.get(GameKeys.LEVITATION, true) && ((LivingEntity) (Object) this).isAlwaysTicking())
            return null;
        return self.getEffect(statusEffect);
    }

    @Redirect(method = "travelInAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getEffectiveGravity()D"))
    private double travelHasNoGravityProxy(LivingEntity self) {
        if (self.hasEffect(MobEffects.LEVITATION) && !GameMap.INSTANCE.get(GameKeys.LEVITATION, true) && ((LivingEntity) (Object) this).isAlwaysTicking())
            return 0;
        return this.getEffectiveGravity();
    }

    @Unique
    private float height = -1;

    @Unique
    @Override
    public void _setStepHeight(float height) {
        this.height = height;
    }

    @Inject(method = "maxUpStep", at = @At("HEAD"), cancellable = true)
    private void onStepHeightRedirect(CallbackInfoReturnable<Float> cir) {
        if (height > 0) {
            cir.setReturnValue(height);
        }
    }

    private float airStrafingSpeed = 0.02f;

    @ModifyConstant(method = "getFlyingSpeed", constant = @Constant(floatValue = 0.02f))
    private float getAirStrafeSpeed(float constant) {
        return airStrafingSpeed;
    }

    @Unique
    @Override
    public float getAirStrafingSpeed() {
        return airStrafingSpeed;
    }

    @Unique
    @Override
    public void setAirStrafingSpeed(float value) {
        airStrafingSpeed = value;
    }

}
