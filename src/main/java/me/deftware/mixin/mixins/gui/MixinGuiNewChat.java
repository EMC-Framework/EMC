package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.chat.hud.HudLine;
import me.deftware.mixin.imp.IMixinGuiNewChat;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class MixinGuiNewChat implements IMixinGuiNewChat {

    @Shadow
    @Final
    private List<ChatHudLine<Text>> messages;

    @Shadow
    @Final
    private List<ChatHudLine<OrderedText>> visibleMessages;

    @Shadow
    protected abstract void addMessage(Text chatComponent, int messageId, int timestamp, boolean displayOnly);

    @Override
    public void setTheChatLine(Text chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        addMessage(chatComponent, chatLineId, updateCounter, displayOnly);
    }

    @Override
    public void removeMessage(HudLine line) {
        String text = line.getMessage().toString(false);
        messages.removeIf(message -> message.getText().getString().equalsIgnoreCase(text));
        visibleMessages.removeIf(message -> {
            StringBuilder builder = new StringBuilder();
            message.getText().accept((index, style, codePoint) -> {
                builder.append((char) codePoint);
                return true;
            });
            return builder.toString().equalsIgnoreCase(text);
        });
    }

    @Override
    public void removeLine(int index) {
        messages.remove(index);
        visibleMessages.remove(index);
    }

    @Override
    public List<HudLine> getLines() {
        List<HudLine> list = new ArrayList<>();
        for (int index = 0; index < messages.size(); index++) {
            ChatHudLine<Text> line = messages.get(index);
            //if (line.getText() instanceof Text) {
                list.add(new HudLine(new ChatMessage().fromText(line.getText()),  index));
            //}
        }
        return list;
    }

}
