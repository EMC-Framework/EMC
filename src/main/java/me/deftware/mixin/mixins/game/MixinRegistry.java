package me.deftware.mixin.mixins.game;

import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public class MixinRegistry {

	@Inject(method = "register(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("HEAD"))
	private static <V, T extends V> void register(Registry<V> registry, Identifier id, T entry, CallbackInfoReturnable<T> cir) {
		if (entry instanceof Block) {
			BlockRegistry.INSTANCE.register(id.toString(), (Block) entry);
		} else if (entry instanceof Item) {
			ItemRegistry.INSTANCE.register(id.toString(), (Item) entry);
		}
	}

}
