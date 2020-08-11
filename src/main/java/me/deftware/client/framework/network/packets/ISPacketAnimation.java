package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.IPacket;
import me.deftware.client.framework.wrappers.entity.IEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketAnimation;

public class ISPacketAnimation extends IPacket {

	public ISPacketAnimation(Packet<?> packet) {
		super(packet);
	}

	public int getEntityID() {
		return ((SPacketAnimation) packet).getEntityID();
	}

	public int getAnimationID() {
		return ((SPacketAnimation) packet).getAnimationType();
	}

	public IEntity getEntity() {
		return IEntity.fromEntity(Minecraft.getMinecraft().theWorld.getEntityByID(getEntityID()));
	}

}
