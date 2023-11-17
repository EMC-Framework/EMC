package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.packet.Packet;

public class SPacketWorldTime extends PacketWrapper {

	public SPacketWorldTime(Packet<?> packet) {
		super(packet);
	}

}
