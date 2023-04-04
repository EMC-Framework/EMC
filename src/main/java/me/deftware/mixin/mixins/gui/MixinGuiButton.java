package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.widgets.Button;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author Deftware
 */
@Mixin(ButtonWidget.class)
public class MixinGuiButton extends MixinClickableWidget implements Button {

    @Override
    public Message getComponentLabel() {
        return (Message) ((ClickableWidget) (Object) this).getMessage();
    }

    @Override
    public Button setComponentLabel(Message text) {
        ((ClickableWidget) (Object) this).setMessage((Text) text);
        return this;
    }

}
