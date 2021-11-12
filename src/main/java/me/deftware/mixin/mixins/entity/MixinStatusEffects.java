package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.StatusEffectRegistry;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Potion.class)
public class MixinStatusEffects {

	@Redirect(method = "registerPotions", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/RegistryNamespaced;register(ILjava/lang/Object;Ljava/lang/Object;)V", opcode = 180))
	private static void registerPotions(RegistryNamespaced<ResourceLocation, Potion> registryNamespaced, int id, Object key, Object value) {
		StatusEffectRegistry.INSTANCE.register(((ResourceLocation) key).getPath(), (Potion) value);
		registryNamespaced.register(id, (ResourceLocation) key, (Potion) value);
	}


}
