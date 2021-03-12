package me.deftware.client.framework.network.packets;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author Deftware
 */
public class CPacketPositionRotation extends CPacketPlayer {

	public CPacketPositionRotation(Packet<?> packet) {
		super(packet);
	}

	public CPacketPositionRotation(double x, double y, double z, float yaw, float pitch, boolean isOnGround) {
		super(new C03PacketPlayer.C06PacketPlayerPosLook(x,y,z,yaw,pitch,isOnGround));
	}

}
