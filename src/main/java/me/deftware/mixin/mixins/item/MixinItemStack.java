package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.item.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public class MixinItemStack {

	@Shadow @Final private net.minecraft.item.Item item;

	@Inject(method = "getTooltip", at = @At(value = "TAIL"), cancellable = true)
	private void onGetTooltipFromItem(EntityPlayer playerIn, boolean advanced, CallbackInfoReturnable<List<String>> cir) {
		List<ChatMessage> list = new ArrayList<>();
		for (String text : cir.getReturnValue()) {
			list.add(new ChatMessage().fromString(text));
		}
		EventGetItemToolTip event = new EventGetItemToolTip(list, Item.newInstance(item), Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
		event.broadcast();
		List<String> modifiedTextList = new ArrayList<>();
		for (ChatMessage text : event.getList()) {
			modifiedTextList.add(text.toString(true));
		}
		cir.setReturnValue(modifiedTextList);
	}

}
