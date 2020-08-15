package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.IPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ICPacketRotation extends IPacket {

	public ICPacketRotation(Packet<?> packet) {
		super(packet);
	}

	public ICPacketRotation(float yaw, float pitch, boolean onGround) {
		super(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, onGround));
	}

}
