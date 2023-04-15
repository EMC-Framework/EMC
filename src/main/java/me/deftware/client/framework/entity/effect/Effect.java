package me.deftware.client.framework.entity.effect;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.registry.Identifiable;

public interface Effect extends Identifiable {

    Type getType();

    Message getName();

    enum Type {
        BENEFICIAL,
        HARMFUL,
        NEUTRAL
    }

}
