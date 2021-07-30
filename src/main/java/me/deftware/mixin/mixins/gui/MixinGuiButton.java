package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.Drawable;
import net.minecraft.client.gui.GuiButton;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.widgets.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Deftware
 */
@Mixin(GuiButton.class)
public class MixinGuiButton implements Button, Drawable {

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
        return ((GuiButton) (Object) this).getWidth();
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
    public ChatMessage getComponentLabel() {
        return new ChatMessage().fromString(
                ((GuiButton) (Object) this).displayString
        );
    }

    @Override
    public Button setComponentLabel(ChatMessage text) {
        ((GuiButton) (Object) this).displayString = text.toString(true);
        return this;
    }

    @Unique
    private List<String> tooltipComponents;

    @Override
    public Button _setTooltip(ChatMessage... tooltip) {
        this.tooltipComponents = Arrays.stream(tooltip)
                .map(m -> m.toString(true))
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public List<String> _getTooltip() {
        return tooltipComponents;
    }

    @Override
    public void onRender(int mouseX, int mouseY, float delta) {
        ((GuiButton) (Object) this).render(mouseX, mouseY, delta);
    }

}
