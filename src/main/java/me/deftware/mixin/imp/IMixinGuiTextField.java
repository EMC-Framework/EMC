package me.deftware.mixin.imp;

import me.deftware.client.framework.fonts.EMCFont;
import net.minecraft.client.gui.FontRenderer;

import java.util.function.BiFunction;

public interface IMixinGuiTextField {

    int getHeight();

    void setHeight(int height);

    FontRenderer getFontRendererInstance();

    int getCursorCounter();

    int getSelectionEnd();

    int getLineScrollOffset();

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    int getWidth();

    void setWidth(int width);

    void setUseMinecraftScaling(boolean state);

    void setUseCustomFont(boolean state);

    void setCustomFont(EMCFont font);

    int getMaxTextLength();

    boolean getHasBorder();

    boolean getIsEditble();

    BiFunction<String, Integer, String> getRenderTextProvider();

    String getSuggestion();

    int getCursorMax();

    void setOverlay(boolean flag);

}
