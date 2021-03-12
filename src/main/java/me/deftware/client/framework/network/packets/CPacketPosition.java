package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class CPacketPosition extends CPacketPlayer {

    public CPacketPosition(Packet<?> packet) {
        super(packet);
    }

    public CPacketPosition(double xIn, double yIn, double zIn, boolean onGroundIn) {
        super(new net.minecraft.network.play.client.CPacketPlayer.Position(xIn, yIn, zIn, onGroundIn));
    }

}
