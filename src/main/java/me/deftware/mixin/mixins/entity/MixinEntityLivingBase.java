package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventIsPotionActive;
import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase implements IMixinEntityLivingBase {

    @Shadow
    @Final
    private Map<Potion, PotionEffect> activePotionsMap;

    @Shadow
    private int activeItemStackUseCount;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "isPotionActive", at = @At(value = "TAIL"), cancellable = true)
    private void onHasStatusEffect(Potion effect, CallbackInfoReturnable<Boolean> cir) {
        if (!((EntityLivingBase) (Object) this instanceof EntityPlayerSP)) {
            return;
        }

        EventIsPotionActive event = new EventIsPotionActive(effect.getName(), activePotionsMap.containsKey(effect));
        event.broadcast();
        cir.setReturnValue(event.isActive());
    }

    @Redirect(method = "travel", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Block;slipperiness:F", opcode = 180))
    public float slipperiness(Block block) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Slipperiness, block.slipperiness);
        event.broadcast();
        return event.getMultiplier();
    }

    @Inject(method = "getJumpUpwardsMotion", at = @At(value = "TAIL"), cancellable = true)
    private void onGetJumpVelocity(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(GameMap.INSTANCE.get(GameKeys.JUMP_HEIGHT, cir.getReturnValue()));
    }

    @Override
    public int getActiveItemStackUseCount() {
        return activeItemStackUseCount;
    }

}
