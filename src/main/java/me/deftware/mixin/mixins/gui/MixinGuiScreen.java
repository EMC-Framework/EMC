package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.gui.widgets.NativeComponent;
import me.deftware.client.framework.gui.widgets.GenericComponent;
import me.deftware.client.framework.registry.ItemRegistry;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
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
    protected MinecraftClient client;

    @Shadow
    @Final
    protected List<ClickableWidget> buttons;

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

    @Inject(method = "getTooltipFromItem", at = @At(value = "TAIL"))
    private void onGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> list = cir.getReturnValue();
        new EventGetItemToolTip(list, ItemRegistry.INSTANCE.getItem(stack.getItem()), client.options.advancedItemTooltips).broadcast();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onDraw(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        GLX.INSTANCE.refresh();
        event.setMouseX(mouseX);
        event.setMouseY(mouseY);
        event.setType(EventScreen.Type.Draw).broadcast();
        // Draw drawables
        for (Drawable drawable : this.drawables) {
            drawable.render(matrices, mouseX, mouseY, delta);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "render", at = @At("RETURN"))
    private void onPostDraw(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!((Object) this instanceof HandledScreen)) {
            this.onPostDrawEvent(matrices, mouseX, mouseY, delta);
        }
    }

    @Unique
    protected void onPostDrawEvent(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        event.setType(EventScreen.Type.PostDraw).broadcast();
        // Render tooltip
        for (Element element : children) {
            if (element instanceof Tooltipable) {
                Tooltipable tooltipable = (Tooltipable) element;
                if (tooltipable.isMouseOverComponent(mouseX, mouseY)) {
                    List<OrderedText> list = tooltipable.getTooltipComponents(mouseX, mouseY);
                    if (list != null && !list.isEmpty()) {
                        this.renderTooltip(mouseX, mouseY, list);
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

    @Override
    public void renderTooltip(int x, int y, List<OrderedText> tooltipComponents) {
        ((Screen) (Object) this).renderOrderedTooltip(GLX.INSTANCE.getStack(), tooltipComponents, x, y);
    }

}
