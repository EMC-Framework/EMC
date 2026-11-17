package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

/**
 * @author Deftware
 */
public class CPacketRotation extends CPacketPlayer {

    public CPacketRotation(Packet<?> packet) {
        super(packet);
    }

    public CPacketRotation(float yaw, float pitch, boolean onGround) {
        super(new ServerboundMovePlayerPacket.Rot(yaw, pitch, onGround, false));
    }

}
