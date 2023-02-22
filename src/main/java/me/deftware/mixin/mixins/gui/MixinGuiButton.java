package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.widgets.Button;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author Deftware
 */
@Mixin(ButtonWidget.class)
public class MixinGuiButton extends MixinClickableWidget implements Button {

    @Override
    public ChatMessage getComponentLabel() {
        return new ChatMessage().fromText(
                ((ClickableWidget) (Object) this).getMessage()
        );
    }

    @Override
    public Button setComponentLabel(ChatMessage text) {
        ((ClickableWidget) (Object) this).setMessage(text.build());
        return this;
    }

}
