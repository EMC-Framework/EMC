package me.deftware.client.framework.event.events;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.item.Item;
import java.util.List;

public class EventGetItemToolTip extends Event {
    private final List<ChatMessage> list;
    private final Item item;
    private final boolean advanced;

    public EventGetItemToolTip(List<ChatMessage> list, Item item, boolean advanced) {
        this.list = list;
        this.item = item;
        this.advanced = advanced;
    }

    public List<ChatMessage> getList() {
        return this.list;
    }

    public Item getItem() {
        return this.item;
    }

    public boolean isAdvanced() {
        return this.advanced;
    }
}
