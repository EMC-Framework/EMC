package me.deftware.client.framework.item;

import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.ItemRenderer;

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
    default void render(int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
        ItemRenderer.getInstance().drawItem(x, y + 5, this);
        FontRenderer.drawString(getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
    }

}
