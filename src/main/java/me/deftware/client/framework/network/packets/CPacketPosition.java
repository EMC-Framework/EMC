package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author Deftware
 */
public class CPacketPosition extends PacketWrapper {

    public CPacketPosition(Packet<?> packet) {
        super(packet);
    }

    public CPacketPosition(double xIn, double yIn, double zIn, boolean onGroundIn) {
        super(new C03PacketPlayer.C04PacketPlayerPosition(xIn, yIn, zIn, onGroundIn));
    }

}
