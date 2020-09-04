package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.EntityS2CPacket;
import net.minecraft.network.Packet;

import javax.annotation.Nullable;

/**
 * @author Deftware
 */
public class SPacketEntity extends PacketWrapper {

	public SPacketEntity(Packet<?> packet) {
		super(packet);
	}

	public boolean isOnGround() {
		return ((EntityS2CPacket) packet).isOnGround();
	}

	@Nullable
	public Entity getEntity() {
		net.minecraft.entity.Entity entity = ((EntityS2CPacket) packet).getEntity(MinecraftClient.getInstance().world);
		return entity == null ? null : Entity.newInstance(entity);
	}

}
