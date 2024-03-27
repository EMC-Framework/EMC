package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.GameChat;
import me.deftware.client.framework.message.Message;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Function;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements GameChat {

    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @Shadow
    public abstract void addMessage(Text message, MessageSignatureData messageSignatureData, MessageIndicator messageIndicator);

    @Unique
    @Override
    public void remove(Function<String, Boolean> visitor) {
        visibleMessages.removeIf(line -> {
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
        addMessage((Text) message, null, null);
    }

    @Unique
    @Override
    public void remove(int index) {
        visibleMessages.remove(index);
    }

}
