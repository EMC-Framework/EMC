package me.deftware.mixin.mixins.gui;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SignEditScreen.class)
public abstract class MixinGuiEditSign extends MixinGuiScreen implements me.deftware.client.framework.gui.screens.SignEditScreen {

    @Final
    @Shadow
    private SignBlockEntity sign;

    @Shadow
    private int currentRow;

    @Override
    public int _getCurrentLine() {
        return currentRow;
    }

    @Override
    public String _getLine(int line) {
        return sign.getTextOnRow(line).getString();
    }

    @Override
    public void _setLine(int line, String newText) {
        sign.setTextOnRow(line, new LiteralText(newText));
    }

    @Override
    public void _save() {
        sign.markDirty();
    }

}
