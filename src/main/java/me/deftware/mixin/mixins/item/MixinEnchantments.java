package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.registry.EnchantmentRegistry;
import me.deftware.mixin.imp.IdentifiableResource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class MixinEnchantments implements IdentifiableResource {

	private ResourceLocation resourceLocation;

	@Inject(method = "<init>(ILnet/minecraft/util/ResourceLocation;ILnet/minecraft/enchantment/EnumEnchantmentType;)V", at = @At("RETURN"))
	private void onCreate(int enchID, ResourceLocation enchName, int enchWeight, EnumEnchantmentType enchType, CallbackInfo ci) {
		this.resourceLocation = enchName;
		EnchantmentRegistry.INSTANCE.register(enchName.getResourcePath(), (Enchantment) (Object) this);
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return resourceLocation;
	}

}
