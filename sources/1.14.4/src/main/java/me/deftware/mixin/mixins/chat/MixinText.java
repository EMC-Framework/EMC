package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.Message;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Text.class)
public interface MixinText extends Message {

}
