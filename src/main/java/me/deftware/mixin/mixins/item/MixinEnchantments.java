package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class MixinEnchantments {

	@Inject(method = "<init>(ILnet/minecraft/util/ResourceLocation;ILnet/minecraft/enchantment/EnumEnchantmentType;)V", at = @At("RETURN"))
	private void onCreate(int enchID, ResourceLocation enchName, int enchWeight, EnumEnchantmentType enchType, CallbackInfo ci) {
		EnchantmentRegistry.INSTANCE.register(enchName.toString(), (Enchantment) (Object) this);
	}

}
