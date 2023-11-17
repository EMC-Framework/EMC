package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class CPacketUseEntity extends PacketWrapper {

    public CPacketUseEntity(Packet<?> packet) {
        super(packet);
    }

    public CPacketUseEntity(Entity entity) {
        super(new C02PacketUseEntity(entity.getMinecraftEntity(), C02PacketUseEntity.Action.ATTACK));
    }

    public Type getType() {
        switch (((C02PacketUseEntity) packet).getAction()) {
            case ATTACK:
                return Type.ATTACK;
            case INTERACT:
                return Type.INTERACT;
            case INTERACT_AT:
                return Type.INTERACT_AT;
        }
        return Type.UNKNOWN;
    }

    public enum Type {
        ATTACK, INTERACT, INTERACT_AT, UNKNOWN
    }

}
