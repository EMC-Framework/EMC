package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.IPacket;
import me.deftware.mixin.imp.IMixinCPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ICPacketPlayer extends IPacket {

	public ICPacketPlayer(Packet<?> packet) {
		super(packet);
	}

	public ICPacketPlayer() {
		super(new C03PacketPlayer());
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
