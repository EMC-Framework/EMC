package me.deftware.client.framework.network.packets;

import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class CPacketPositionRotation extends CPacketPlayer {

	public CPacketPositionRotation(Packet<?> packet) {
		super(packet);
	}

	public CPacketPositionRotation(double x, double y, double z, float yaw, float pitch, boolean isOnGround) {
		super(new net.minecraft.network.play.client.CPacketPlayer.PositionRotation(x,y,z,yaw,pitch,isOnGround));
	}

}
