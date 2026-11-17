package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

/**
 * @author Deftware
 */
public class CPacketPosition extends CPacketPlayer {

    public CPacketPosition(Packet<?> packet) {
        super(packet);
    }

    public CPacketPosition(double xIn, double yIn, double zIn, boolean onGroundIn) {
        super(new ServerboundMovePlayerPacket.Pos(xIn, yIn, zIn, onGroundIn, false));
    }

}
