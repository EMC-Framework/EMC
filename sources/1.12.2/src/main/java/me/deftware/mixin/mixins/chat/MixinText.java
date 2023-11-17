package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.Message;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ITextComponent.class)
public interface MixinText extends Message {

    @Override
    default String getString() {
        return ((ITextComponent) this).getUnformattedText();
    }

}
