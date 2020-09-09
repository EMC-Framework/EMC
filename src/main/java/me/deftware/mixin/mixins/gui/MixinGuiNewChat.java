package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.chat.hud.ChatHudLine;
import me.deftware.client.framework.event.events.EventChatReceive;
import me.deftware.mixin.imp.IMixinGuiNewChat;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
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
import java.util.Optional;

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
    protected abstract void addMessage(StringRenderable chatComponent, int messageId, int timestamp, boolean displayOnly);

    @Override
    public void setTheChatLine(LiteralText chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        addMessage(chatComponent, chatLineId, updateCounter, displayOnly);
    }

    @Override
    public void removeMessage(ChatHudLine line) {
        String text = line.getMessage().toString(false);
        messages.removeIf(message -> message.getText().getString().equalsIgnoreCase(text));
        visibleMessages.removeIf(message -> {
            StringBuilder builder = new StringBuilder();
            message.getText().visit((string) -> {
                builder.append(string);
                return Optional.empty();
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
    public List<ChatHudLine> getLines() {
        List<ChatHudLine> list = new ArrayList<>();
        for (int index = 0; index < messages.size(); index++) {
            net.minecraft.client.gui.hud.ChatHudLine line = messages.get(index);
            if (line.getText() instanceof LiteralText) {
                list.add(new ChatHudLine(new ChatMessage().fromString(line.getText().getString()),  index));
            }
        }
        return list;
    }

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    public Text addMessage(Text chatComponent) {
        event = new EventChatReceive(new ChatMessage().fromText(chatComponent)).broadcast();
        return event.getMessage().build();
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/StringRenderable;IIZ)V", at = @At("HEAD"), cancellable = true)
    public void addMessage(StringRenderable chatComponent, int messageId, int timestamp, boolean displayOnly, CallbackInfo ci) {
        if (event != null && event.isCanceled()) {
            ci.cancel();
        }
    }

}
