package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.client.framework.world.ClientWorld;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class SPacketAnimation extends PacketWrapper {

	public SPacketAnimation(Packet<?> packet) {
		super(packet);
	}

	public int getEntityID() {
		return ((net.minecraft.network.play.server.S0BPacketAnimation) packet).getEntityID();
	}

	public int getAnimationID() {
		return ((net.minecraft.network.play.server.S0BPacketAnimation) packet).getAnimationType();
	}

	public Entity getEntity() {
		return ClientWorld.getClientWorld()._getEntityById(getEntityID());
	}

}
