package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantment.class)
public class MixinEnchantments {

	@Redirect(method = "registerEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/RegistryNamespaced;register(ILjava/lang/Object;Ljava/lang/Object;)V", opcode = 180))
	private static void registerEnchantments(RegistryNamespaced<ResourceLocation, Enchantment> registryNamespaced, int id, Object key, Object value) {
		EnchantmentRegistry.INSTANCE.register(((ResourceLocation) key).toString(), (Enchantment) value);
		registryNamespaced.register(id, (ResourceLocation) key, (Enchantment) value);
	}

}
