package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.play.client.C02PacketUseEntity;

/**
 * @author Deftware
 */
public class CPacketUseEntity extends PacketWrapper {

    public CPacketUseEntity(Entity entity) {
        super(new C02PacketUseEntity(entity.getMinecraftEntity(), C02PacketUseEntity.Action.ATTACK));
    }

}
