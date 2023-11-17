package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.widgets.Component;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClickableWidget.class)
public class MixinClickableWidget implements Component, Tooltipable {

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

    @Unique
    private final List<OrderedText> tooltipComponents = new ArrayList<>();

    @Override
    public List<OrderedText> getTooltipComponents(int mouseX, int mouseY) {
        return this.tooltipComponents;
    }

    @Override
    public boolean isMouseOverComponent(int mouseX, int mouseY) {
        return ((ClickableWidget) (Object) this).isHovered();
    }

    @Override
    public void setPosition(int x, int y) {
        setPositionX(x);
        setPositionY(y);
    }

}
