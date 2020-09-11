package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;

/**
 * @author Deftware
 */
public class CPacketRotation extends PacketWrapper {

    public CPacketRotation(Packet<?> packet) {
        super(packet);
    }

    public CPacketRotation(float yaw, float pitch, boolean onGround) {
        super(new PlayerMoveC2SPacket.LookOnly(yaw, pitch, onGround));
    }

}
