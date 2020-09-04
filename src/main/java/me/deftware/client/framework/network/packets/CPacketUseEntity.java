package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.server.network.packet.PlayerInteractEntityC2SPacket;

/**
 * @author Deftware
 */
public class CPacketUseEntity extends PacketWrapper {

    public CPacketUseEntity(Entity entity) {
        super(new PlayerInteractEntityC2SPacket(entity.getMinecraftEntity()));
    }

}
