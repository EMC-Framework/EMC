package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.GameChat;
import me.deftware.client.framework.message.Message;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
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
    private List<ChatHudLine<OrderedText>> visibleMessages;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow protected abstract void addMessage(Text message, int messageId, int timestamp, boolean refresh);

    @Unique
    @Override
    public void remove(Function<String, Boolean> visitor) {
        visibleMessages.removeIf(line -> {
            StringBuilder text = new StringBuilder();
            line.getText().accept((index, style, point) -> {
                text.appendCodePoint(point);
                return true;
            });
            return visitor.apply(text.toString());
        });
    }

    @Unique
    @Override
    public void append(Message message) {
        addMessage((Text) message, 0, this.client.inGameHud.getTicks(), false);
    }

    @Unique
    @Override
    public void remove(int index) {
        visibleMessages.remove(index);
    }

}
