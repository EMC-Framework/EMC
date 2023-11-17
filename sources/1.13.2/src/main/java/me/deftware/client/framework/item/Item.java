package me.deftware.client.framework.item;

import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.ItemRendering;
import me.deftware.client.framework.render.gl.GLX;

/**
 * @author Deftware
 */
public interface Item extends Itemizable, SelectableList.ListItem {

    int getID();

    Message getName();

    boolean isFood();

    int getHunger();

    float getSaturation();

    @Override
    default void render(GLX context, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
        ItemRendering.getInstance().drawItem(context, x, y + 5, this);
        FontRenderer.drawString(context, getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
    }

}
