package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class MixinEnchantments {

	/*@Inject(method = "register", at = @At("HEAD")) FIXME
	private static void register(String nameIn, Enchantment enchantmentIn, CallbackInfo ci) {
		EnchantmentRegistry.INSTANCE.register(nameIn, enchantmentIn);
	}*/

}
