package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.conversion.CachedSupplier;
import me.deftware.client.framework.conversion.InstanceList;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.event.events.EventGuiScreenDraw;
import me.deftware.client.framework.event.events.EventGuiScreenPostDraw;
import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.gui.minecraft.ScreenInstance;
import me.deftware.client.framework.gui.widgets.Button;
import me.deftware.client.framework.item.Item;
import me.deftware.mixin.imp.IMixinGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(net.minecraft.client.gui.GuiScreen.class)
public class MixinGuiScreen implements IMixinGuiScreen {

    @Unique
    protected boolean shouldSendPostRenderEvent = true;

    @Unique
    protected InstanceList<Button, GuiButton> emcButtonList =
            new InstanceList<>(() -> this.buttonList, button -> button instanceof Button, button -> (Button) button);

    @Shadow
    protected FontRenderer fontRenderer;

    @Shadow
    @Final
    protected List<GuiButton> buttonList;

    @Shadow
    protected Minecraft mc;

    @Unique
    protected CachedSupplier<ScreenInstance> screenInstance = new CachedSupplier<>(() -> {
        if (!(((net.minecraft.client.gui.GuiScreen) (Object) this) instanceof GuiScreen)) {
            return ScreenInstance.newInstance((net.minecraft.client.gui.GuiScreen) (Object) this);
        }
        return null;
    });

    @Unique
    @Override
    public List<Button> getEmcButtons() {
        return emcButtonList.poll();
    }

    @Override
    public List<GuiButton> getButtonList() {
        return buttonList;
    }

    @Override
    public FontRenderer getFont() {
        return fontRenderer;
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void render(int x, int y, float tickDelta, CallbackInfo ci) {
        if (!(((net.minecraft.client.gui.GuiScreen) (Object) this) instanceof GuiScreen)) {
            new EventGuiScreenDraw(screenInstance.get(), x, y).broadcast();
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void render_return(int x, int y, float tickDelta, CallbackInfo ci) {
        if (shouldSendPostRenderEvent && !(((net.minecraft.client.gui.GuiScreen) (Object) this) instanceof GuiScreen)) {
            new EventGuiScreenPostDraw(screenInstance.get(), x, y).broadcast();
        }
    }

    @Inject(method = "getItemToolTip", at = @At(value = "TAIL"), cancellable = true)
    private void onGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<String>> cir) {
        List<ChatMessage> list = new ArrayList<>();
        for (String text : cir.getReturnValue()) {
            list.add(new ChatMessage().fromString(text));
        }
        EventGetItemToolTip event = new EventGetItemToolTip(list, Item.newInstance(stack.getItem()), mc.gameSettings.advancedItemTooltips);
        event.broadcast();
        List<String> modifiedTextList = new ArrayList<>();
        for (ChatMessage text : event.getList()) {
            modifiedTextList.add(text.toString(true));
        }
        cir.setReturnValue(modifiedTextList);
    }

}
