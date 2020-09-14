package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author Deftware
 */
public class CPacketRotation extends PacketWrapper {

    public CPacketRotation(Packet<?> packet) {
        super(packet);
    }

    public CPacketRotation(float yaw, float pitch, boolean onGround) {
        super(new CPacketPlayer.Rotation(yaw, pitch, onGround));
    }

}
