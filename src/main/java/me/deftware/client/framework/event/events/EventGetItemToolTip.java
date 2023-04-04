package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.message.Message;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.function.Function;

public class EventGetItemToolTip extends Event {

    private final List<ITextComponent> list;
    private final Item item;

    private final boolean advanced;

    public EventGetItemToolTip(List<ITextComponent> list, Item item, boolean advanced) {
        this.list = list;
        this.item = item;
        this.advanced = advanced;
    }

    public void remove(Function<Message, Boolean> visitor) {
        list.removeIf(text -> visitor.apply((Message) text));
    }

    public List<ITextComponent> getList() {
        return list;
    }

    public Item getItem() {
        return item;
    }

    public boolean isAdvanced() {
        return advanced;
    }

}
