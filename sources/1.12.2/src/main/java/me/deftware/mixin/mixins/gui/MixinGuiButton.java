package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.Drawable;
import me.deftware.client.framework.gui.Element;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import me.deftware.client.framework.message.MessageUtils;
import me.deftware.client.framework.gui.widgets.Button;
import me.deftware.client.framework.message.Message;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Deftware
 */
@Mixin(GuiButton.class)
public class MixinGuiButton implements Button, Drawable, Element {

    @Shadow
    protected int height;

    @Override
    public int getPositionX() {
        return ((GuiButton) (Object) this).x;
    }

    @Override
    public int getPositionY() {
        return ((GuiButton) (Object) this).y;
    }

    @Override
    public int getComponentWidth() {
        return ((GuiButton) (Object) this).getButtonWidth();
    }

    @Override
    public int getComponentHeight() {
        return this.height;
    }

    @Override
    public boolean isActive() {
        return ((GuiButton) (Object) this).enabled;
    }

    @Override
    public void setPositionX(int x) {
        ((GuiButton) (Object) this).x = x;
    }

    @Override
    public void setPositionY(int y) {
        ((GuiButton) (Object) this).y = y;
    }

    @Override
    public void setComponentWidth(int width) {
        ((GuiButton) (Object) this).setWidth(width);
    }

    @Override
    public void setComponentHeight(int height) {
        this.height = height;
    }

    @Override
    public void setActive(boolean state) {
        ((GuiButton) (Object) this).enabled = state;
    }

    @Override
    public Message getComponentLabel() {
        return MessageUtils.parse(((GuiButton) (Object) this).displayString);
    }

    @Override
    public Button setComponentLabel(Message text) {
        ((GuiButton) (Object) this).displayString = (((ITextComponent) text).getFormattedText());
        return this;
    }

    @Unique
    private final List<String> tooltipComponents = new ArrayList<>();

    @Override
    public List<String> getTooltipComponents(int mouseX, int mouseY) {
        return this.tooltipComponents;
    }

    @Override
    public void onRender(int mouseX, int mouseY, float delta) {
        ((GuiButton) (Object) this).drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
    }

    @Override
    public void onMouseReleased(int x, int y, int button) {
        ((GuiButton) (Object) this).mouseReleased(x, y);
    }

    @Override
    public void click() {
        ((MinecraftScreen) Minecraft.getMinecraft().currentScreen)
                .clickButton((GuiButton) (Object) this);
    }

}
