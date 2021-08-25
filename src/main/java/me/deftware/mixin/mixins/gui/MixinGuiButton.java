package me.deftware.mixin.mixins.gui;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.widgets.Button;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Deftware
 */
@Mixin(AbstractButtonWidget.class)
public class MixinGuiButton implements Button {

    @Shadow
    protected int height;

    @Override
    public int getPositionX() {
        return ((AbstractButtonWidget) (Object) this).x;
    }

    @Override
    public int getPositionY() {
        return ((AbstractButtonWidget) (Object) this).y;
    }

    @Override
    public int getComponentWidth() {
        return ((AbstractButtonWidget) (Object) this).getWidth();
    }

    @Override
    public int getComponentHeight() {
        return ((AbstractButtonWidget) (Object) this).getHeight();
    }

    @Override
    public boolean isActive() {
        return ((AbstractButtonWidget) (Object) this).active;
    }

    @Override
    public void setPositionX(int x) {
        ((AbstractButtonWidget) (Object) this).x = x;
    }

    @Override
    public void setPositionY(int y) {
        ((AbstractButtonWidget) (Object) this).y = y;
    }

    @Override
    public void setComponentWidth(int width) {
        ((AbstractButtonWidget) (Object) this).setWidth(width);
    }

    @Override
    public void setComponentHeight(int height) {
        this.height = height;
    }

    @Override
    public void setActive(boolean state) {
        ((AbstractButtonWidget) (Object) this).active = state;
    }

    @Override
    public ChatMessage getComponentLabel() {
        return new ChatMessage().fromText(
                ((AbstractButtonWidget) (Object) this).getMessage()
        );
    }

    @Override
    public Button setComponentLabel(ChatMessage text) {
        ((AbstractButtonWidget) (Object) this).setMessage(text.build());
        return this;
    }

    @Unique
    private final List<OrderedText> tooltipComponents = new ArrayList<>();

    @Override
    public List<OrderedText> getTooltipComponents(int mouseX, int mouseY) {
        return this.tooltipComponents;
    }

}
