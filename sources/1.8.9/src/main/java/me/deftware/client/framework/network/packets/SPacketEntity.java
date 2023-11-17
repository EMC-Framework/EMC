package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.Minecraft;
import me.deftware.client.framework.world.ClientWorld;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;

/**
 * @author Deftware
 */
public class SPacketEntity extends PacketWrapper {

	public SPacketEntity(Packet<?> packet) {
		super(packet);
	}

	public boolean isOnGround() {
		return ((S14PacketEntity) packet).getOnGround();
	}

	public Entity getEntity() {
		net.minecraft.entity.Entity entity = ((S14PacketEntity) packet).getEntity(Minecraft.getMinecraft().theWorld);
		return ClientWorld.getClientWorld().getEntityByReference(
				entity
		);
	}

}
