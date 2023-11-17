package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.message.Message;

import java.util.List;
import java.util.function.Function;

public class EventGetItemToolTip extends Event {

    private final List<Message> list;
    private final Item item;

    private final boolean advanced;

    public EventGetItemToolTip(List<Message> list, Item item, boolean advanced) {
        this.list = list;
        this.item = item;
        this.advanced = advanced;
    }

    public void remove(Function<Message, Boolean> visitor) {
        list.removeIf(text -> visitor.apply((Message) text));
    }

    public List<Message> getList() {
        return list;
    }

    public Item getItem() {
        return item;
    }

    public boolean isAdvanced() {
        return advanced;
    }

}
