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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
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
    private List<Renderable> renderables;

    @Shadow
    @Final
    private List<GuiEventListener> children;

    @Shadow
    protected Minecraft minecraft;

    @Shadow
    protected abstract void clearWidgets();

    @Override
    public <T extends GenericComponent> List<T> getChildren(Class<T> clazz) {
        return this.children.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void _clearChildren() {
        this.clearWidgets();
    }

    @Override
    public void addScreenComponent(GenericComponent component, int index) {
        if (component instanceof NativeComponent<?> nativeComponent) {
            component = nativeComponent.getComponent();
        }
        if (component instanceof Renderable drawable) {
            appendArray(renderables, drawable, index);
        }
        if (component instanceof GuiEventListener element) {
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

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void onDraw(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        event.setMouseX(mouseX);
        event.setMouseY(mouseY);
        event.setType(EventScreen.Type.Draw).setContext(GLX.of(context)).broadcast();
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "extractRenderStateWithTooltipAndSubtitles", at = @At("RETURN"))
    private void onPostDraw(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!((Object) this instanceof AbstractContainerScreen)) {
            this.onPostDrawEvent(context, mouseX, mouseY, delta);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/network/chat/Component;)V", at = @At("RETURN"))
    private void init(Component title, CallbackInfo ci) {
        event.setType(EventScreen.Type.Init).broadcast();
    }

    @Inject(method = "init(II)V", at = @At("RETURN"))
    private void init(int width, int height, CallbackInfo ci) {
        event.setType(EventScreen.Type.Setup).broadcast();
    }

    @Unique
    protected void onPostDrawEvent(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        event.setType(EventScreen.Type.PostDraw).setContext(GLX.of(context)).broadcast();
        // Render tooltip
        for (GuiEventListener element : children) {
            if (element instanceof Tooltipable tooltipable) {
                if (tooltipable.isMouseOverComponent(mouseX, mouseY)) {
                    List<FormattedCharSequence> list = tooltipable.getTooltipComponents(mouseX, mouseY);
                    if (list != null && !list.isEmpty()) {
                        this.renderTooltip(GLX.of(context), mouseX, mouseY, list);
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "defaultHandleGameClickEvent", at = @At("HEAD"), cancellable = true)
    private static void onTextClick(ClickEvent event, Minecraft minecraft, Screen screen, CallbackInfo ci) {
        if (event instanceof ClickEvent.RunCommand(String command)) {
            String trigger = CommandRegister.getCommandTrigger();
            if (command.startsWith(trigger)) {
                try {
                    var networkHandler = minecraft.getConnection();
                    if (networkHandler != null) {
                        var source = networkHandler.getSuggestionsProvider();
                        CommandRegister.getDispatcher().execute(command.substring(trigger.length()), source);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                ci.cancel();
            }
        }
    }

}
