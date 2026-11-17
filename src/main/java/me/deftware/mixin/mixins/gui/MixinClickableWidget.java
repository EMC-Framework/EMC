package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.widgets.Component;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractWidget.class)
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
        return ((AbstractWidget) (Object) this).getWidth();
    }

    @Override
    public int getComponentHeight() {
        return ((AbstractWidget) (Object) this).getHeight();
    }

    @Unique
    @Override
    public boolean isActive() {
        return ((AbstractWidget) (Object) this).active;
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
        ((AbstractWidget) (Object) this).setWidth(width);
    }

    @Override
    public void setComponentHeight(int height) {
        this.height = height;
    }

    @Override
    public void setActive(boolean state) {
        ((AbstractWidget) (Object) this).active = state;
    }

    @Unique
    private final List<FormattedCharSequence> tooltipComponents = new ArrayList<>();

    @Override
    public List<FormattedCharSequence> getTooltipComponents(int mouseX, int mouseY) {
        return this.tooltipComponents;
    }

    @Override
    public boolean isMouseOverComponent(int mouseX, int mouseY) {
        return ((AbstractWidget) (Object) this).isHovered();
    }

    @Unique
    @Override
    public void setPosition(int x, int y) {
        setPositionX(x);
        setPositionY(y);
    }

}
