package me.deftware.mixin.mixins;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.chat.hud.ChatHudLine;
import me.deftware.client.framework.event.events.EventChatReceive;
import me.deftware.mixin.imp.IMixinGuiNewChat;
import net.minecraft.ChatFormat;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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

@Mixin(ChatHud.class)
public abstract class MixinGuiNewChat implements IMixinGuiNewChat {

    @Shadow
    @Final
    private List<net.minecraft.client.gui.hud.ChatHudLine> messages;

    @Shadow
    @Final
    private List<net.minecraft.client.gui.hud.ChatHudLine> visibleMessages;

    @Unique
    private EventChatReceive event;

    @Shadow
    protected abstract void addMessage(Component component_1, int int_1, int int_2, boolean boolean_1);

    @Override
    public void setTheChatLine(TextComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        addMessage(chatComponent, chatLineId, updateCounter, displayOnly);
    }

    @Override
    public void removeMessage(ChatHudLine line) {
        messages.removeIf(message -> ChatFormat.stripFormatting(message.getContents().getString()).equalsIgnoreCase(line.getMessage().toString(false)));
        visibleMessages.removeIf(message -> ChatFormat.stripFormatting(message.getContents().getString()).equalsIgnoreCase(line.getMessage().toString(false)));
    }

    @Override
    public void removeLine(int index) {
        messages.remove(index);
        visibleMessages.remove(index);
    }

    @Override
    public List<ChatHudLine> getLines() {
        List<ChatHudLine> list = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            net.minecraft.client.gui.hud.ChatHudLine line = messages.get(i);
            if (line.getContents() instanceof TextComponent) {
                list.add(new ChatHudLine(new ChatMessage().fromText(line.getContents()),  i));
            }
        }
        return list;
    }

    @ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;I)V", at = @At("HEAD"))
    public Component addMessage(Component chatComponent) {
        event = new EventChatReceive(new ChatMessage().fromText(chatComponent)).broadcast();
        return event.getMessage().build();
    }

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V", at = @At("HEAD"), cancellable = true)
    public void addMessage(Component textComponent_1, int int_1, int int_2, boolean boolean_1, CallbackInfo ci) {
        if (event != null && event.isCanceled()) {
            ci.cancel();
        }
    }

}
