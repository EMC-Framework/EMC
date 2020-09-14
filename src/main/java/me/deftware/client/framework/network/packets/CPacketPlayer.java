package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.mixin.imp.IMixinCPacketPlayer;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class CPacketPlayer extends PacketWrapper {

    public CPacketPlayer(Packet<?> packet) {
        super(packet);
    }

    public CPacketPlayer() {
        super(new net.minecraft.network.play.client.CPacketPlayer());
    }

    public void setOnGround(boolean state) {
        ((IMixinCPacketPlayer) getPacket()).setOnGround(state);
    }

    public void setY(double y) {
        ((IMixinCPacketPlayer) getPacket()).setY(y);
    }

    public double getY() {
        return ((IMixinCPacketPlayer) getPacket()).getY();
    }

    public void setMoving(boolean state) {
        ((IMixinCPacketPlayer) getPacket()).setMoving(state);
    }

}
