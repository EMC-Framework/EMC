package me.deftware.mixin.mixins.gui;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiEditSign.class)
public abstract class MixinGuiEditSign extends MixinGuiScreen implements me.deftware.client.framework.gui.screens.SignEditScreen  {

    @Final
    @Shadow
    private TileEntitySign tileSign;

    @Shadow
    private int editLine;

    @Override
    public int _getCurrentLine() {
        return editLine;
    }

    @Override
    public String _getLine(int line) {
        return tileSign.getText(line).getString();
    }

    @Override
    public void _setLine(int line, String newText) {
        tileSign.setText(line, new TextComponentString(newText));
    }

    @Override
    public void _save() {
        tileSign.markDirty();
    }

}
