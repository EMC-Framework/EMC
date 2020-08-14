package me.deftware.client.framework.event.events;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.wrappers.item.IItem;
import java.util.List;

public class EventGetItemToolTip extends Event {
    private final List<ChatMessage> list;
    private final IItem item;

    public EventGetItemToolTip(List<ChatMessage> list, IItem item) {
        this.list = list;
        this.item = item;
    }

    public List<ChatMessage> getList() {
        return this.list;
    }

    public IItem getItem() {
        return this.item;
    }
}
