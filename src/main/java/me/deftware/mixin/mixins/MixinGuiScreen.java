package me.deftware.mixin.mixins;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.event.events.EventGuiScreenDraw;
import me.deftware.client.framework.event.events.EventGuiScreenPostDraw;
import me.deftware.client.framework.wrappers.item.IItem;
import me.deftware.mixin.imp.IMixinGuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiScreen.class)
public class MixinGuiScreen implements IMixinGuiScreen {

    public boolean shouldSendPostRenderEvent = true;

    @Shadow
    protected FontRenderer fontRenderer;

    @Shadow
    protected List<GuiButton> buttonList;

    @Override
    public List<GuiButton> getButtonList() {
        return buttonList;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void drawScreen(int x, int y, float p_render_3_, CallbackInfo ci) {
        new EventGuiScreenDraw((GuiScreen) (Object) this, x, y).broadcast();
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawScreen_return(int x, int y, float p_render_3_, CallbackInfo ci) {
        if (shouldSendPostRenderEvent) {
            new EventGuiScreenPostDraw((GuiScreen) (Object) this, x, y).broadcast();
        }
    }

    @Inject(method = "getItemToolTip", at = @At(value = "TAIL"), cancellable = true)
    private void onGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<String>> cir) {
        List<ChatMessage> list = new ArrayList<>();
        for (String text : cir.getReturnValue()) {
            list.add(new ChatMessage().fromString(text));
        }
        EventGetItemToolTip event = new EventGetItemToolTip(list, new IItem(stack.getItem()));
        event.broadcast();
        List<String> modifiedTextList = new ArrayList<>();
        for (ChatMessage text : event.getList()) {
            modifiedTextList.add(text.toString(true));
        }
        cir.setReturnValue(modifiedTextList);
    }

}
