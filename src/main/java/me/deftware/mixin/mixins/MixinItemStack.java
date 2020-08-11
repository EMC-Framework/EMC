package me.deftware.mixin.mixins;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.wrappers.item.IItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Final
    @Shadow
    private Item item;

    @Redirect(method = "addEnchantment", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setShort(Ljava/lang/String;S)V"))
    public void putShort(NBTTagCompound compoundTag, String str, short sh, Enchantment enchantment, int level)
    {
        compoundTag.setShort(str, (short)level);
    }

    @Inject(method = "getTooltip", at = @At(value = "TAIL"), cancellable = true)
    private void onGetTooltipFromItem(EntityPlayer playerIn, boolean advanced, CallbackInfoReturnable<List<String>> cir) {
        List<ChatMessage> list = new ArrayList<>();
        List<String> returnList = new ArrayList<>();
        for (String tooltipChunk : cir.getReturnValue()) {
            list.add(new ChatMessage().fromString(tooltipChunk));
        }
        EventGetItemToolTip event = new EventGetItemToolTip(list, new IItem(item));
        event.broadcast();
        for (ChatMessage chatMessage : event.getList()) {
            returnList.add(chatMessage.toString(true));
        }
        cir.setReturnValue(returnList);
    }
}
