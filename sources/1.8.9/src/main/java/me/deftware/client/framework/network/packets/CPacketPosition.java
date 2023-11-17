package me.deftware.client.framework.network.packets;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author Deftware
 */
public class CPacketPosition extends CPacketPlayer {

    public CPacketPosition(Packet<?> packet) {
        super(packet);
    }

    public CPacketPosition(double xIn, double yIn, double zIn, boolean onGroundIn) {
        super(new C03PacketPlayer.C04PacketPlayerPosition(xIn, yIn, zIn, onGroundIn));
    }

}
