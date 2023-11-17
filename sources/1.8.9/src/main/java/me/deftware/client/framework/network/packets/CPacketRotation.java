package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author Deftware
 */
public class CPacketRotation extends CPacketPlayer {

    public CPacketRotation(Packet<?> packet) {
        super(packet);
    }

    public CPacketRotation(float yaw, float pitch, boolean onGround) {
        super(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, onGround));
    }

}
