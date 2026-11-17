package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractSignEditScreen.class)
public abstract class MixinGuiEditSign extends MixinGuiScreen implements me.deftware.client.framework.gui.screens.SignEditScreen {

    @Final
    @Shadow
    private SignBlockEntity sign;

    @Shadow
    private int line;

    @Shadow
    @Final
    private SignText.Mutable text;

    @Shadow
    @Final
    private net.minecraft.world.level.block.entity.SignTextSlot slot;

    @Shadow
    @Final
    private String[] messages;

    @Override
    public int _getCurrentLine() {
        return line;
    }

    @Override
    public String _getLine(int line) {
        return text.asImmutable().getMessages(false).get(line).getString();
    }

    @Override
    public void _setLine(int line, String text) {
        Message message = Message.of(text);
        messages[line] = text;
        this.text.setLine(line, (Component) message);
        sign.setText(this.text.asImmutable(), this.slot);
    }

    @Override
    public void _save() {
        sign.setChanged();
    }

}
