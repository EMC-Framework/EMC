package me.deftware.mixin.mixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Redirect(method = "addEnchantment", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setShort(Ljava/lang/String;S)V"))
    public void putShort(NBTTagCompound compoundTag, String str, short sh, Enchantment enchantment, int level)
    {
        compoundTag.setShort(str, (short)level);
    }
}
