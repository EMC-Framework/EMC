package me.deftware.mixin.mixins.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {

    /* @Inject(method = "getLevelFromNbt", at = @At("HEAD"), cancellable = true)
    private static void onGetLevel(NbtCompound nbt, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.max(nbt.getInt("lvl"), 0));
    } */

}
