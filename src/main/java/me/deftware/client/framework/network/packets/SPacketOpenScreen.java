package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;

public class SPacketOpenScreen extends PacketWrapper {

    public SPacketOpenScreen(Packet<?> packet) {
        super(packet);
    }

    public int getSyncId() {
        return ((ClientboundOpenScreenPacket) this.packet).getContainerId();
    }

}
