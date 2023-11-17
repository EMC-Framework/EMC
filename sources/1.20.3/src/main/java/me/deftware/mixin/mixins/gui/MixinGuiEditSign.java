package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.Message;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractSignEditScreen.class)
public abstract class MixinGuiEditSign extends MixinGuiScreen implements me.deftware.client.framework.gui.screens.SignEditScreen {

    @Final
    @Shadow
    private SignBlockEntity blockEntity;

    @Shadow
    private int currentRow;

    @Shadow
    private SignText text;

    @Shadow
    @Final
    private boolean front;

    @Shadow
    @Final
    private String[] messages;

    @Override
    public int _getCurrentLine() {
        return currentRow;
    }

    @Override
    public String _getLine(int line) {
        return text.getMessage(line, false).getString();
    }

    @Override
    public void _setLine(int line, String text) {
        Message message = Message.of(text);
        messages[line] = text;
        this.text = this.text.withMessage(line, (Text) message);
        blockEntity.setText(this.text, this.front);
    }

    @Override
    public void _save() {
        blockEntity.markDirty();
    }

}
