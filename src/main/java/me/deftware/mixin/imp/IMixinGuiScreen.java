package me.deftware.mixin.imp;

import me.deftware.client.framework.gui.widgets.Button;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import java.util.List;

public interface IMixinGuiScreen {

    List<Button> getEmcButtons();

    List<GuiButton> getButtonList();

    FontRenderer getFont();

}
