package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;

/**
 * @author Deftware
 */
public class CPacketUseEntity extends PacketWrapper {

    public CPacketUseEntity(Entity entity) {
        super(new net.minecraft.network.play.client.CPacketUseEntity(entity.getMinecraftEntity()));
    }

}
