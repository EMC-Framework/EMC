package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.gui.widgets.NativeComponent;
import me.deftware.client.framework.gui.widgets.GenericComponent;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Deftware
 */
@Mixin(Screen.class)
public abstract class MixinGuiScreen implements MinecraftScreen {

    @Unique
    private final EventScreen event = new EventScreen((Screen) (Object) this);

    @Shadow
    @Final
    private List<Drawable> drawables;

    @Shadow
    @Final
    private List<Element> children;

    @Shadow
    protected MinecraftClient client;

    @Shadow
    protected abstract void clearChildren();

    @Override
    public <T extends GenericComponent> List<T> getChildren(Class<T> clazz) {
        return this.children.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void _clearChildren() {
        this.clearChildren();
    }

    @Override
    public void addScreenComponent(GenericComponent component, int index) {
        if (component instanceof NativeComponent<?> nativeComponent) {
            component = nativeComponent.getComponent();
        }
        if (component instanceof Drawable drawable) {
            appendArray(drawables, drawable, index);
        }
        if (component instanceof Element element) {
            appendArray(children, element, index);
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

    @Inject(method = "render", at = @At("HEAD"))
    private void onDraw(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        event.setMouseX(mouseX);
        event.setMouseY(mouseY);
        event.setType(EventScreen.Type.Draw).setContext(GLX.of(context)).broadcast();
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "renderWithTooltip", at = @At("RETURN"))
    private void onPostDraw(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!((Object) this instanceof HandledScreen)) {
            this.onPostDrawEvent(context, mouseX, mouseY, delta);
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Text title, CallbackInfo ci) {
        event.setType(EventScreen.Type.Init).broadcast();
    }

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        event.setType(EventScreen.Type.Setup).broadcast();
    }

    @Unique
    protected void onPostDrawEvent(DrawContext context, int mouseX, int mouseY, float delta) {
        event.setType(EventScreen.Type.PostDraw).setContext(GLX.of(context)).broadcast();
        // Render tooltip
        for (Element element : children) {
            if (element instanceof Tooltipable tooltipable) {
                if (tooltipable.isMouseOverComponent(mouseX, mouseY)) {
                    List<OrderedText> list = tooltipable.getTooltipComponents(mouseX, mouseY);
                    if (list != null && !list.isEmpty()) {
                        this.renderTooltip(GLX.of(context), mouseX, mouseY, list);
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "handleTextClick", at = @At("HEAD"), cancellable = true)
    private void onTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        var event = style.getClickEvent();
        if (event != null) {
            if (event.getAction() == ClickEvent.Action.RUN_COMMAND) {
                String text = SharedConstants.stripInvalidChars(event.getValue());
                String trigger = CommandRegister.getCommandTrigger();
                if (text.startsWith(trigger)) {
                    try {
                        var source = MinecraftClient.getInstance().player.getCommandSource();
                        CommandRegister.getDispatcher().execute(text.substring(trigger.length()), source);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    cir.setReturnValue(true);
                }
            }
        }
    }

}
