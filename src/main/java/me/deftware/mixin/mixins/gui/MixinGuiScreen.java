package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.gui.Drawable;
import me.deftware.client.framework.gui.Element;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.gui.widgets.NativeComponent;
import me.deftware.client.framework.gui.widgets.GenericComponent;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.message.MessageUtils;
import me.deftware.client.framework.item.Item;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ConstantConditions")
@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen implements MinecraftScreen {

    @Unique
    private final EventScreen event = new EventScreen((GuiScreen) (Object) this);

    @Unique
    private final List<Drawable> drawables = new ArrayList<>();

    @Unique
    protected final List<Element> children = new ArrayList<>();

    @Shadow
    protected List<GuiButton> buttonList;

    @Shadow
    protected abstract void actionPerformed(GuiButton button) throws IOException;

    @Override
    public <T extends GenericComponent> List<T> getChildren(Class<T> clazz) {
        return Stream.concat(this.children.stream(), this.buttonList.stream())
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void _clearChildren() {
        drawables.clear();
        children.clear();
        buttonList.clear();
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

    @Redirect(method = "getItemToolTip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/client/util/ITooltipFlag;)Ljava/util/List;"))
    private List<String> onGetTooltip(ItemStack stack, EntityPlayer player, ITooltipFlag context) {
        List<Message> list = stack.getTooltip(player, context).stream().map(MessageUtils::parse).collect(Collectors.toList());
        EventGetItemToolTip event = new EventGetItemToolTip(list, (Item) stack.getItem(), context.isAdvanced()).broadcast();
        return event.getList().stream().map(ITextComponent.class::cast).map(ITextComponent::getFormattedText).collect(Collectors.toList());
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
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
    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void onPostDraw(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!((Object) this instanceof GuiContainer)) {
            this.onPostDrawEvent(mouseX, mouseY, delta);
        }
    }

    @Unique
    protected void onPostDrawEvent(int mouseX, int mouseY, float delta) {
        event.setType(EventScreen.Type.PostDraw).broadcast();
        // Render tooltip
        for (Element element : children) {
            if (element instanceof Tooltipable) {
                Tooltipable tooltipable = (Tooltipable) element;
                if (tooltipable.isMouseOverComponent(mouseX, mouseY)) {
                    List<String> list = tooltipable.getTooltipComponents(mouseX, mouseY);
                    if (list != null && !list.isEmpty()) {
                        this.renderTooltip(mouseX, mouseY, list);
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

    @Inject(method = "keyTyped", at = @At("HEAD"))
    private void onKeyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        for (Element element : this.children) {
            element.onKeyTyped(typedChar, keyCode);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        for (Element element : this.children) {
            element.onMouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void onMouseReleased(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        for (Element element : this.children) {
            element.onMouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Inject(method = "handleMouseInput", at = @At("HEAD"))
    private void onHandleMouse(CallbackInfo ci) {
        for (Element element : this.children) {
            if (element instanceof SelectableList) {
                ((SelectableList<?>) element).handleMouseInput();
            }
        }
    }

    @Override
    public void renderTooltip(int x, int y, List<String> tooltipComponents) {
        ((GuiScreen) (Object) this).drawHoveringText(tooltipComponents, x, y);
    }

    @Override
    public void clickButton(GuiButton button) {
       try {
           this.actionPerformed(button);
       } catch (Exception ex) {
           ex.printStackTrace();
       }
    }

}
