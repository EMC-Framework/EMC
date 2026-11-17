package me.deftware.client.framework.item;

import com.google.common.util.concurrent.AtomicDouble;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.ItemRendering;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

    default net.minecraft.world.item.Item asMinecraftItem() {
        return (net.minecraft.world.item.Item) this;
    }

    static void modifiers(Item item, Predicate<Identifier> predicate, Consumer<Double> consumer) {
        var component = item.asMinecraftItem().components()
                .get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (component != null) {
            var modifiers = component.modifiers();
            for (ItemAttributeModifiers.Entry entry : modifiers) {
                var modifier = entry.modifier();
                if (!predicate.test(modifier.id())) {
                    continue;
                }
                consumer.accept(modifier.amount());
            }
        }
    }

    static float damage(Item item) {
        AtomicDouble sum = new AtomicDouble();
        modifiers(item, id -> id.equals(net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID), sum::addAndGet);
        return (float) sum.get();
    }

}
