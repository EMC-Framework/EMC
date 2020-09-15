package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.StatusEffectRegistry;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Potion.class)
public class MixinStatusEffects {

	@Inject(method = "<init>(ILnet/minecraft/util/ResourceLocation;ZI)V", at = @At("RETURN"))
	private void onCreate(int potionID, ResourceLocation location, boolean badEffect, int potionColor, CallbackInfo ci) {
		StatusEffectRegistry.INSTANCE.register(location.toString(), (Potion) (Object) this);
	}

}
