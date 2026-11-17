package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.GameChat;
import me.deftware.client.framework.message.Message;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Function;

@Mixin(ChatComponent.class)
public abstract class MixinChatHud implements GameChat {

    @Shadow
    @Final
    private List<GuiMessage.Line> trimmedMessages;

    @Shadow
    public abstract void addClientSystemMessage(Component message);

    @Unique
    @Override
    public void remove(Function<String, Boolean> visitor) {
        trimmedMessages.removeIf(line -> {
            var text = new StringBuilder();
            line.content().accept((index, style, point) -> {
                text.appendCodePoint(point);
                return true;
            });
            return visitor.apply(text.toString());
        });
    }

    @Unique
    @Override
    public void append(Message message) {
        addClientSystemMessage((Component) message);
    }

    @Unique
    @Override
    public void remove(int index) {
        trimmedMessages.remove(index);
    }

}
