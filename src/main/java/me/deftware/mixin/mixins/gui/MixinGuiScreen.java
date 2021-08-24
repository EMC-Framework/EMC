package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.gui.Drawable;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.gui.widgets.NativeComponent;
import me.deftware.client.framework.gui.widgets.GenericComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IGuiEventListener;
import me.deftware.client.framework.registry.ItemRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
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
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
@Mixin(GuiScreen.class)
public class MixinGuiScreen implements MinecraftScreen {

    @Unique
    private final EventScreen event = new EventScreen((GuiScreen) (Object) this);

    @Unique
    private final List<Drawable> drawables = new ArrayList<>();

    @Shadow
    @Final
    protected List<IGuiEventListener> children;

    @Shadow
    @Final
    protected List<GuiButton> buttons;

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
        if (component instanceof IGuiEventListener) {
            appendArray(children, (IGuiEventListener) component, index);
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

    @Inject(method = "getItemToolTip", at = @At(value = "TAIL"), cancellable = true)
    private void onGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<String>> cir) {
        List<ChatMessage> list = cir.getReturnValue().stream()
                .map(t -> new ChatMessage().fromString(t))
                .collect(Collectors.toList());
        new EventGetItemToolTip(list, ItemRegistry.INSTANCE.getItem(stack.getItem()), Minecraft.getInstance().gameSettings.advancedItemTooltips).broadcast();
        cir.setReturnValue(
                list.stream().map(ChatMessage::toString).collect(Collectors.toList())
        );
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onDraw(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        event.setMouseX(mouseX);
        event.setMouseY(mouseY);
        event.setType(EventScreen.Type.Draw).broadcast();
        // Draw drawables
        for (Drawable drawable : this.drawables) {
            drawable.onRender(mouseX, mouseY, delta);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "render", at = @At("RETURN"))
    private void onPostDraw(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!((Object) this instanceof GuiContainer)) {
            this.onPostDrawEvent(mouseX, mouseY, delta);
        }
    }

    @Unique
    protected void onPostDrawEvent(int mouseX, int mouseY, float delta) {
        event.setType(EventScreen.Type.PostDraw).broadcast();
        // Render tooltip
        for (IGuiEventListener element : children) {
            if (element instanceof GuiButton && element instanceof Tooltipable) {
                if (((GuiButton) element).isMouseOver()) {
                    List<String> list = ((Tooltipable<?>) element)._getTooltip();
                    if (list != null && !list.isEmpty()) {
                        ((net.minecraft.client.gui.GuiScreen) (Object) this).drawHoveringText(list, mouseX, mouseY);
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        event.setType(EventScreen.Type.Init).broadcast();
    }

    @Inject(method = "setWorldAndResolution(Lnet/minecraft/client/Minecraft;II)V", at = @At("HEAD"))
    private void initHead(CallbackInfo ci) {
        drawables.clear();
    }

    @Inject(method = "setWorldAndResolution(Lnet/minecraft/client/Minecraft;II)V", at = @At("RETURN"))
    private void initReturn(CallbackInfo ci) {
        event.setType(EventScreen.Type.Setup).broadcast();
    }

}
