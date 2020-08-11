package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.IPacket;
import me.deftware.client.framework.wrappers.entity.IEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;

public class ISPacketEntity extends IPacket {

	public ISPacketEntity(Packet<?> packet) {
		super(packet);
	}

	public boolean isOnGround() {
		return ((SPacketEntity) packet).getOnGround();
	}

	public IEntity getEntity() {
		return IEntity.fromEntity(((SPacketEntity) packet).getEntity(Minecraft.getMinecraft().theWorld));
	}

}
