package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.MessageUtils;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import me.deftware.client.framework.gui.widgets.Button;
import net.minecraft.client.gui.widget.ButtonWidget;
import me.deftware.client.framework.message.Message;
import net.minecraft.text.Text;
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
        return this.height;
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
    public Message getComponentLabel() {
        return MessageUtils.parse(((AbstractButtonWidget) (Object) this).getMessage());
    }

    @Override
    public Button setComponentLabel(Message text) {
        ((AbstractButtonWidget) (Object) this).setMessage(((Text) text).asFormattedString());
        return this;
    }

    @Unique
    private final List<String> tooltipComponents = new ArrayList<>();

    @Override
    public List<String> getTooltipComponents(int mouseX, int mouseY) {
        return this.tooltipComponents;
    }

    @Override
    public void click() {
        ((ButtonWidget) (Object) this).onPress();
    }

}
