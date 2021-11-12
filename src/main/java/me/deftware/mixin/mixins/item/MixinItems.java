package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.registry.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class MixinItems {

	@Inject(method = "registerItem(ILnet/minecraft/util/ResourceLocation;Lnet/minecraft/item/Item;)V", at = @At("TAIL"))
	private static void register(int id, ResourceLocation resourceLocationIn, Item itemIn, CallbackInfo ci) {
		ItemRegistry.INSTANCE.register(resourceLocationIn.getPath(), itemIn);
	}

}
