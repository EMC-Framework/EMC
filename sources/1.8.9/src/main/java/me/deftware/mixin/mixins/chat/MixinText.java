package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.Message;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IChatComponent.class)
public interface MixinText extends Message {

    @Override
    default String getString() {
        return ((IChatComponent) this).getUnformattedText();
    }

}
