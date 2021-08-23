package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(GuiContainerCreative.class)
public abstract class MixinCreativeInventoryScreen {

    /**
     * Uses {@link MixinGuiScreen#onGetTooltipFromItem(ItemStack, CallbackInfoReturnable)}
     * to get a modified tooltip. Only called in the search tab in a creative inventory
     */
    @Redirect(method = "renderToolTip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/client/util/ITooltipFlag;)Ljava/util/List;"))
    private List<String> onGetStackTooltip(ItemStack itemStack, EntityPlayer playerIn, ITooltipFlag advanced) {
        return (((GuiScreen) (Object) this).getItemToolTip(itemStack));
    }

}
