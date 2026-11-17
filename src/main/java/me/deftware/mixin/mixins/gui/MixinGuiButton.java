package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import me.deftware.client.framework.gui.widgets.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Deftware
 */
@Mixin(net.minecraft.client.gui.components.Button.class)
public class MixinGuiButton extends MixinClickableWidget implements Button {

    @Override
    public Message getComponentLabel() {
        return (Message) ((AbstractWidget) (Object) this).getMessage();
    }

    @Override
    public Button setComponentLabel(Message text) {
        ((AbstractWidget) (Object) this).setMessage((Component) text);
        return this;
    }

    @Unique
    @Override
    public void click() {
        ((net.minecraft.client.gui.components.Button) (Object) this).onPress(new MouseButtonInfo(0, 0));
    }

}
