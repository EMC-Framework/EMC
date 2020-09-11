package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author Deftware
 */
public class CPacketPosition extends PacketWrapper {

    public CPacketPosition(Packet<?> packet) {
        super(packet);
    }

    public CPacketPosition(double xIn, double yIn, double zIn, boolean onGroundIn) {
        super(new CPacketPlayer.Position(xIn, yIn, zIn, onGroundIn));
    }

}
