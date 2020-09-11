package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;

/**
 * @author Deftware
 */
public class CPacketPosition extends PacketWrapper {

    public CPacketPosition(Packet<?> packet) {
        super(packet);
    }

    public CPacketPosition(double xIn, double yIn, double zIn, boolean onGroundIn) {
        super(new PlayerMoveC2SPacket.PositionOnly(xIn, yIn, zIn, onGroundIn));
    }

}
