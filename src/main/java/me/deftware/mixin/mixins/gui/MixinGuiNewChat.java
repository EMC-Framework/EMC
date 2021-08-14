package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.chat.hud.HudLine;
import me.deftware.client.framework.event.events.EventChatReceive;
import me.deftware.mixin.imp.IMixinGuiNewChat;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
    private List<ChatHudLine> messages;

    @Shadow
    @Final
    private List<ChatHudLine> visibleMessages;

    @Unique
    private EventChatReceive event;

    @Shadow
    protected abstract void addMessage(Text chatComponent, int messageId, int timestamp, boolean displayOnly);

    @Override
    public void setTheChatLine(LiteralText chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        addMessage(chatComponent, chatLineId, updateCounter, displayOnly);
    }

    @Override
    public void removeMessage(HudLine line) {
        messages.removeIf(message -> Formatting.strip(message.getText().getString()).equalsIgnoreCase(line.getMessage().toString(false)));
        visibleMessages.removeIf(message -> Formatting.strip(message.getText().getString()).equalsIgnoreCase(line.getMessage().toString(false)));
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
            ChatHudLine line = messages.get(index);
            if (line.getText() instanceof LiteralText) {
                list.add(new HudLine(new ChatMessage().fromString(line.getText().getString()),  index));
            }
        }
        return list;
    }

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    public Text addMessage(Text chatComponent) {
        event = new EventChatReceive(new ChatMessage().fromText(chatComponent)).broadcast();
        return event.getMessage().build();
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("HEAD"), cancellable = true)
    public void addMessage(Text chatComponent, int messageId, int timestamp, boolean displayOnly, CallbackInfo ci) {
        if (event != null && event.isCanceled()) {
            ci.cancel();
        }
    }

}
