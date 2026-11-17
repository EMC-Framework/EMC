package me.deftware.client.framework.network.packets;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

/**
 * @author Deftware
 */
public class CPacketPositionRotation extends CPacketPlayer {

	public CPacketPositionRotation(Packet<?> packet) {
		super(packet);
	}

	public CPacketPositionRotation(double x, double y, double z, float yaw, float pitch, boolean isOnGround) {
		super(new ServerboundMovePlayerPacket.PosRot(x,y,z,yaw,pitch,isOnGround, false));
	}

}
