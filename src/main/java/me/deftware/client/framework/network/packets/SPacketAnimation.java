package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class SPacketAnimation extends PacketWrapper {

	public SPacketAnimation(Packet<?> packet) {
		super(packet);
	}

	public int getEntityID() {
		return ((net.minecraft.network.play.server.SPacketAnimation) packet).getEntityID();
	}

	public int getAnimationID() {
		return ((net.minecraft.network.play.server.SPacketAnimation) packet).getAnimationType();
	}

	public Entity getEntity() {
		return Entity.newInstance(Minecraft.getInstance().world.getEntityByID(getEntityID()));
	}

}
