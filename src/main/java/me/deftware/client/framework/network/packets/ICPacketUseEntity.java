package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.IPacket;
import me.deftware.client.framework.wrappers.entity.IEntity;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class ICPacketUseEntity extends IPacket {

	public ICPacketUseEntity(IEntity entity) {
		super(new C02PacketUseEntity(entity.getEntity(), C02PacketUseEntity.Action.ATTACK));
	}

}
