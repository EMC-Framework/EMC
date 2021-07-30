package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.screens.MinecraftScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(GuiContainerCreative.class)
public abstract class MixinCreativeInventoryScreen {

    /**
     * Uses {@link MixinGuiScreen#onGetTooltipFromItem(ItemStack, CallbackInfoReturnable)}
     * to get a modified tooltip. Only called in the search tab in a creative inventory
     */
    @Redirect(method = "renderToolTip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/EntityPlayer;Z)Ljava/util/List;"))
    private List<String> onGetStackTooltip(ItemStack itemStack, EntityPlayer playerIn, boolean advanced) {
        return ((MinecraftScreen) this)._getItemStackTooltip(itemStack);
    }

}
