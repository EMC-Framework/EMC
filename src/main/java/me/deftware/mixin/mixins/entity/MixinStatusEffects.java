package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.StatusEffectRegistry;
import me.deftware.mixin.imp.IdentifiableResource;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Potion.class)
public class MixinStatusEffects implements IdentifiableResource {

	private ResourceLocation resourceLocation;

	@Inject(method = "<init>(ILnet/minecraft/util/ResourceLocation;ZI)V", at = @At("RETURN"))
	private void onCreate(int potionID, ResourceLocation location, boolean badEffect, int potionColor, CallbackInfo ci) {
		this.resourceLocation = location;
		StatusEffectRegistry.INSTANCE.register(location.getResourcePath(), (Potion) (Object) this);
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return this.resourceLocation;
	}

}
