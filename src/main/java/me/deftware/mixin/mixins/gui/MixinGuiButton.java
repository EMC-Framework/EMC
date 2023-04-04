package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.widgets.Button;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Deftware
 */
@Mixin(ClickableWidget.class)
public class MixinGuiButton implements Button {

    @Shadow
    protected int height;

    @Shadow
    private int x;

    @Shadow
    private int y;

    @Override
    public int getPositionX() {
        return x;
    }

    @Override
    public int getPositionY() {
        return y;
    }

    @Override
    public int getComponentWidth() {
        return ((ClickableWidget) (Object) this).getWidth();
    }

    @Override
    public int getComponentHeight() {
        return ((ClickableWidget) (Object) this).getHeight();
    }

    @Override
    public boolean isActive() {
        return ((ClickableWidget) (Object) this).active;
    }

    @Override
    public void setPositionX(int x) {
        this.x = x;
    }

    @Override
    public void setPositionY(int y) {
        this.y = y;
    }

    @Override
    public void setComponentWidth(int width) {
        ((ClickableWidget) (Object) this).setWidth(width);
    }

    @Override
    public void setComponentHeight(int height) {
        this.height = height;
    }

    @Override
    public void setActive(boolean state) {
        ((ClickableWidget) (Object) this).active = state;
    }

    @Override
    public Message getComponentLabel() {
        return (Message) ((ClickableWidget) (Object) this).getMessage();
    }

    @Override
    public Button setComponentLabel(Message text) {
        ((ClickableWidget) (Object) this).setMessage((Text) text);
        return this;
    }

    @Unique
    private final List<TooltipComponent> tooltipComponents = new ArrayList<>();

    @Override
    public List<TooltipComponent> getTooltipComponents(int mouseX, int mouseY) {
        return this.tooltipComponents;
    }

}
