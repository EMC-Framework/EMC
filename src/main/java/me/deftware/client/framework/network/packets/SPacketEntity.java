package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class SPacketEntity extends PacketWrapper {

	public SPacketEntity(Packet<?> packet) {
		super(packet);
	}

	public boolean isOnGround() {
		return ((net.minecraft.network.play.server.S14PacketEntity) packet).getOnGround();
	}

	public Entity getEntity() {
		return Entity.newInstance(((net.minecraft.network.play.server.S14PacketEntity) packet).getEntity(Minecraft.getMinecraft().theWorld));
	}

}
