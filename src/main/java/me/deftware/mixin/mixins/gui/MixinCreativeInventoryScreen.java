package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.item.Item;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiContainerCreative.class)
public abstract class MixinCreativeInventoryScreen extends AbstractPlayerInventoryScreen<CreativePlayerInventoryScreen.CreativeContainer> {

    @Unique
    private ItemStack stack;

    @Inject(at = @At("HEAD"), method="renderTooltip")
    public void onRenderTooolip(ItemStack stack, int x, int y, CallbackInfo ci) {
        this.stack = stack;
    }


    /*
    TODO: find a better way of doing this.
    ie. use ModifyVariable on renderTooltip(MatrixStack matrices, List<Text> lines, int x, int y)
    inside of
    renderTooolip(MatrixStack matrices, ItemStack stack, int x, int y);
     */

    @Override
    public void renderTooltip(String textObj, int x, int y) {
        List<ChatMessage> list = new ArrayList<>();
        list.add(new ChatMessage().fromString(textObj));

        EventGetItemToolTip event = new EventGetItemToolTip(list, Item.newInstance(stack.getItem()), minecraft.options.advancedItemTooltips);
        event.broadcast();
        List<String> modifiedTextList = new ArrayList<>();
        for (ChatMessage text : event.getList()) {
            modifiedTextList.add(text.toString(true));
        }
        super.renderTooltip(modifiedTextList.get(0), x, y);
    }



    public MixinCreativeInventoryScreen(CreativePlayerInventoryScreen.CreativeContainer screenHandler, PlayerInventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
    }
}
