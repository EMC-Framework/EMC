package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class CPacketUseEntity extends PacketWrapper {

    public CPacketUseEntity(Packet<?> packet) {
        super(packet);
    }

    public CPacketUseEntity(Entity entity) {
        super(new net.minecraft.network.play.client.CPacketUseEntity(entity.getMinecraftEntity()));
    }

    public Type getType() {
        switch (((net.minecraft.network.play.client.CPacketUseEntity) packet).getAction()) {
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
