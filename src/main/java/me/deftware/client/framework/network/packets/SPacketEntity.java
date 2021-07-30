package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.Minecraft;
import me.deftware.client.framework.world.ClientWorld;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class SPacketEntity extends PacketWrapper {

	public SPacketEntity(Packet<?> packet) {
		super(packet);
	}

	public boolean isOnGround() {
		return ((net.minecraft.network.play.server.SPacketEntity) packet).getOnGround();
	}

	public Entity getEntity() {
		net.minecraft.entity.Entity entity = ((net.minecraft.network.play.server.SPacketEntity) packet).getEntity(Minecraft.getMinecraft().world);
		return ClientWorld.getClientWorld().getEntityByReference(
				entity
		);
	}

}
