package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.client.framework.world.ClientWorld;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;

/**
 * @author Deftware
 */
public class SPacketAnimation extends PacketWrapper {

	public SPacketAnimation(Packet<?> packet) {
		super(packet);
	}

	public int getEntityID() {
		return ((ClientboundAnimatePacket) packet).getId();
	}

	public int getAnimationID() {
		return ((ClientboundAnimatePacket) packet).getAction();
	}

	public Entity getEntity() {
		return ClientWorld.getClientWorld()._getEntityById(getEntityID());
	}

}
