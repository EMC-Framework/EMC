package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class CPacketRotation extends CPacketPlayer {

    public CPacketRotation(Packet<?> packet) {
        super(packet);
    }

    public CPacketRotation(float yaw, float pitch, boolean onGround) {
        super(new net.minecraft.network.play.client.CPacketPlayer.Rotation(yaw, pitch, onGround));
    }

}
