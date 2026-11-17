package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.Message;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Component.class)
public interface MixinText extends Message {

}
