package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.GameChat;
import me.deftware.client.framework.message.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Function;

@Mixin(GuiNewChat.class)
public abstract class MixinChatHud implements GameChat {

    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    protected abstract void setChatLine(ITextComponent message, int messageId, int timestamp, boolean refresh);

    @Unique
    @Override
    public void remove(Function<String, Boolean> visitor) {
        drawnChatLines.removeIf(line -> visitor.apply(line.getChatComponent().getUnformattedText()));
    }

    @Unique
    @Override
    public void append(Message message) {
        setChatLine((ITextComponent) message, 0, this.mc.ingameGUI.getUpdateCounter(), false);
    }

    @Unique
    @Override
    public void remove(int index) {
        drawnChatLines.remove(index);
    }

}
