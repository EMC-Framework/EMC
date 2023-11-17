package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.event.Event;

public class EventStructureLocation extends Event {

    private final Vector3<Double> pos;
    private final StructureType type;

    public EventStructureLocation(double posX, double posY, double posZ, StructureType type) {
        this.pos = Vector3.ofDouble(posX, posY, posZ);
        this.type = type;
    }

    public EventStructureLocation(double posX, double posY, double posZ) {
        this.pos = Vector3.ofDouble(posX, posY, posZ);
        this.type = StructureType.Stronghold;
    }

    public EventStructureLocation(Vector3<Double> pos, StructureType type) {
        this.pos = pos;
        this.type = type;
    }

    public EventStructureLocation(Vector3<Double> pos) {
        this.pos = pos;
        this.type = StructureType.Stronghold;
    }

    public Vector3<Double> getPos() {
        return pos;
    }

    public StructureType getType() {
        return type;
    }

    public enum StructureType {
        Stronghold,
        BuriedTreasure,
        OceanMonument,
        WoodlandMansion,
        OtherMapIcon
    }

}
