package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlocks {

	@Inject(method = "registerBlock(ILnet/minecraft/util/ResourceLocation;Lnet/minecraft/block/Block;)V", at = @At("HEAD"))
	private static void register(int id, ResourceLocation key, Block blockIn, CallbackInfo ci) {
		BlockRegistry.INSTANCE.register(key.getPath(), blockIn);
	}

}
