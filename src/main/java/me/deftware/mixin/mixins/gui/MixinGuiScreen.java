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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
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
@Mixin(Screen.class)
public class MixinGuiScreen implements IMixinGuiScreen {

    @Unique
    protected boolean shouldSendPostRenderEvent = true;

    @Unique
    protected InstanceList<Button, AbstractButtonWidget> emcButtonList =
            new InstanceList<>(() -> this.buttons, button -> button instanceof Button, button -> (Button) button);

    @Shadow
    protected TextRenderer font;

    @Shadow
    @Final
    protected List<AbstractButtonWidget> buttons;

    @Shadow
    @Final
    protected List<Element> children;

    @Shadow
    protected MinecraftClient minecraft;

    @Unique
    protected CachedSupplier<ScreenInstance> screenInstance = new CachedSupplier<>(() -> {
        if (!(((Screen) (Object) this) instanceof GuiScreen)) {
            return ScreenInstance.newInstance((Screen) (Object) this);
        }
        return null;
    });

    @Unique
    @Override
    public List<Button> getEmcButtons() {
        return emcButtonList.poll();
    }

    @Override
    public List<AbstractButtonWidget> getButtonList() {
        return buttons;
    }

    @Override
    public TextRenderer getFont() {
        return font;
    }

    @Override
    public List<Element> getEventList() {
        return children;
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(int x, int y, float tickDelta, CallbackInfo ci) {
        if (!(((Screen) (Object) this) instanceof GuiScreen)) {
            new EventGuiScreenDraw(screenInstance.get(), x, y).broadcast();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void render_return(int x, int y, float tickDelta, CallbackInfo ci) {
        if (shouldSendPostRenderEvent && !(((Screen) (Object) this) instanceof GuiScreen)) {
            new EventGuiScreenPostDraw(screenInstance.get(), x, y).broadcast();
        }
    }

    @Inject(method = "getTooltipFromItem", at = @At(value = "TAIL"), cancellable = true)
    private void onGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<String>> cir) {
        List<ChatMessage> list = new ArrayList<>();
        for (String text : cir.getReturnValue()) {
            list.add(new ChatMessage().fromString(text));
        }
        EventGetItemToolTip event = new EventGetItemToolTip(list, Item.newInstance(stack.getItem()), minecraft.options.advancedItemTooltips);
        event.broadcast();
        List<String> modifiedTextList = new ArrayList<>();
        for (ChatMessage text : event.getList()) {
            modifiedTextList.add(text.toString(true));
        }
        cir.setReturnValue(modifiedTextList);
    }

}
