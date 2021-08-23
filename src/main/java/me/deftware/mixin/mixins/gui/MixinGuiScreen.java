package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.gui.widgets.NativeComponent;
import me.deftware.client.framework.gui.widgets.GenericComponent;
import me.deftware.client.framework.item.Item;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
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
import java.util.stream.Collectors;

/**
 * @author Deftware
 */
@Mixin(Screen.class)
public abstract class MixinGuiScreen implements MinecraftScreen {

    @Unique
    private final EventScreen event = new EventScreen((Screen) (Object) this);

    @Unique
    private final List<Drawable> drawables = new ArrayList<>();

    @Shadow
    @Final
    protected List<Element> children;

    @Shadow
    protected MinecraftClient minecraft;

    @Shadow
    @Final
    protected List<AbstractButtonWidget> buttons;

    @Override
    public <T extends GenericComponent> List<T> getChildren(Class<T> clazz) {
        return this.children.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void _clearChildren() {
        drawables.clear();
        children.clear();
        buttons.clear();
    }

    @Override
    public void addScreenComponent(GenericComponent component, int index) {
        if (component instanceof NativeComponent<?>) {
            component = ((NativeComponent<?>) component).getComponent();
        }
        if (component instanceof Drawable) {
            appendArray(drawables, (Drawable) component, index);
        }
        if (component instanceof Element) {
            appendArray(children, (Element) component, index);
        }
    }

    private <T> void appendArray(List<T> list, T object, int index) {
        if (index < 0 || index > list.size())
            list.add(object);
        else
            list.add(index, object);
    }

    @Override
    public EventScreen getEventScreen() {
        return event;
    }

    @Inject(method = "getTooltipFromItem", at = @At(value = "TAIL"), cancellable = true)
    private void onGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<String>> cir) {
        List<ChatMessage> list = cir.getReturnValue().stream()
                .map(t -> new ChatMessage().fromString(t))
                .collect(Collectors.toList());
        new EventGetItemToolTip(list, Item.newInstance(stack.getItem()), MinecraftClient.getInstance().options.advancedItemTooltips).broadcast();
        cir.setReturnValue(
                list.stream().map(c -> c.toString(true)).collect(Collectors.toList())
        );
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onDraw(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        event.setMouseX(mouseX);
        event.setMouseY(mouseY);
        event.setType(EventScreen.Type.Draw).broadcast();
        // Draw drawables
        for (Drawable drawable : this.drawables) {
            drawable.render(mouseX, mouseY, delta);
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onPostDraw(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        event.setType(EventScreen.Type.PostDraw).broadcast();
        // Render tooltip
        for (Element element : children) {
            if (element instanceof AbstractButtonWidget && element instanceof Tooltipable) {
                if (((AbstractButtonWidget) element).isHovered()) {
                    List<String> list = ((Tooltipable<?>) element)._getTooltip();
                    if (list != null && !list.isEmpty()) {
                        ((Screen) (Object) this).renderTooltip(list, mouseX, mouseY);
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Text title, CallbackInfo ci) {
        event.setType(EventScreen.Type.Init).broadcast();
    }

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("HEAD"))
    private void initHead(CallbackInfo ci) {
        drawables.clear();
    }

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        event.setType(EventScreen.Type.Setup).broadcast();
    }

}
