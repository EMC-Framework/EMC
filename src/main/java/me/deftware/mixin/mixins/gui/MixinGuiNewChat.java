package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.chat.hud.ChatHudLine;
import me.deftware.client.framework.event.events.EventChatReceive;
import me.deftware.mixin.imp.IMixinGuiNewChat;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
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
    protected abstract void setChatLine(IChatComponent component_1, int int_1, int int_2, boolean boolean_1);

    @Override
    public void setTheChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        setChatLine(chatComponent, chatLineId, updateCounter, displayOnly);
    }

    @Override
    public void removeMessage(ChatHudLine line) {
        chatLines.removeIf(message -> EnumChatFormatting.getTextWithoutFormattingCodes(message.getChatComponent().getFormattedText()).equalsIgnoreCase(line.getMessage().toString(false)));
        drawnChatLines.removeIf(message -> EnumChatFormatting.getTextWithoutFormattingCodes(message.getChatComponent().getFormattedText()).equalsIgnoreCase(line.getMessage().toString(false)));
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
            if (line.getChatComponent() instanceof ChatComponentText) {
                list.add(new ChatHudLine(new ChatMessage().fromText(line.getChatComponent(), false),  i));
            }
        }
        return list;
    }

    @ModifyVariable(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"))
    public IChatComponent addMessage(IChatComponent chatComponent) {
        event = new EventChatReceive(new ChatMessage().fromText(chatComponent, true)).broadcast();
        return chatComponent;
    }

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"), cancellable = true)
    public void addMessage(IChatComponent textComponent_1, int int_1, CallbackInfo ci) {
        if (event != null && event.isCanceled()) {
            ci.cancel();
        }
    }

}
