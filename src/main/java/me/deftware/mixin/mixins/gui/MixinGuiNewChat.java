package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.chat.hud.ChatHudLine;
import me.deftware.client.framework.event.events.EventChatReceive;
import me.deftware.mixin.imp.IMixinGuiNewChat;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat implements IMixinGuiNewChat {

    @Shadow
    @Final
    private List<ChatLine> chatLines;

    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;

    @Unique
    private EventChatReceive event;

    @Shadow
    protected abstract void setChatLine(ITextComponent component_1, int int_1, int int_2, boolean boolean_1);

    @Override
    public void setTheChatLine(ITextComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        setChatLine(chatComponent, chatLineId, updateCounter, displayOnly);
    }

    @Override
    public void removeMessage(ChatHudLine line) {
        chatLines.removeIf(message -> TextFormatting.getTextWithoutFormattingCodes(message.getChatComponent().getFormattedText()).equalsIgnoreCase(line.getMessage().toString(false)));
        drawnChatLines.removeIf(message -> TextFormatting.getTextWithoutFormattingCodes(message.getChatComponent().getFormattedText()).equalsIgnoreCase(line.getMessage().toString(false)));
    }

    @Override
    public void removeLine(int index) {
        chatLines.remove(index);
        drawnChatLines.remove(index);
    }

    @Override
    public List<ChatHudLine> getLines() {
        List<ChatHudLine> list = new ArrayList<>();
        for (int i = 0; i < chatLines.size(); i++) {
            ChatLine line = chatLines.get(i);
            if (line.getChatComponent() instanceof TextComponentString) {
                list.add(new ChatHudLine(new ChatMessage().fromText(line.getChatComponent()),  i));
            }
        }
        return list;
    }

    @ModifyVariable(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"))
    public ITextComponent addMessage(ITextComponent chatComponent) {
        event = new EventChatReceive(new ChatMessage().fromText(chatComponent)).broadcast();
        return chatComponent;
    }

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"), cancellable = true)
    public void addMessage(ITextComponent textComponent_1, int int_1, CallbackInfo ci) {
        if (event != null && event.isCanceled()) {
            ci.cancel();
        }
    }

}
