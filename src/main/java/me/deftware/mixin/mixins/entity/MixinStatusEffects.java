package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.StatusEffectRegistry;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Potion.class)
public class MixinStatusEffects {

	@Inject(method = "register", at = @At("HEAD"))
	private static void register(int id, String nameIn, Potion potionIn, CallbackInfo ci) {
		StatusEffectRegistry.INSTANCE.register(nameIn, potionIn);
	}

}
