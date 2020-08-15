package me.deftware.client.framework.network.packets;


import me.deftware.client.framework.network.IPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ICPacketPositionRotation extends IPacket {

	public ICPacketPositionRotation(Packet<?> packet) {
		super(packet);
	}

	public ICPacketPositionRotation(double x, double y, double z, float yaw, float pitch, boolean isOnGround) {
		super(new C03PacketPlayer.C06PacketPlayerPosLook(x,y,z,yaw,pitch,isOnGround));
	}

}
