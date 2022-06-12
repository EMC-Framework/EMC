package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;

public class CPacketChatMessage extends PacketWrapper {

    public CPacketChatMessage(Packet<?> packet) {
        super(packet);
    }

    public CPacketChatMessage(String text) {
        super(new net.minecraft.network.play.client.CPacketChatMessage(text));
    }

}
