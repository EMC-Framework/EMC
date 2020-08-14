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
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
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
    @Final
    protected List<GuiButton> buttons;

    @Shadow
    @Final
    protected List<IGuiEventListener> children;

    @Override
    public List<GuiButton> getButtonList() {
        return buttons;
    }

    @Override
    public FontRenderer getFont() {
        return fontRenderer;
    }

    @Override
    public List<IGuiEventListener> getEventList() {
        return children;
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(int x, int y, float p_render_3_, CallbackInfo ci) {
        new EventGuiScreenDraw((GuiScreen) (Object) this, x, y).broadcast();
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void render_return(int x, int y, float p_render_3_, CallbackInfo ci) {
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
