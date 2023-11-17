package me.deftware.mixin.imp;

import net.minecraft.network.packet.Packet;

public interface IMixinNetworkManager {

	void sendPacketImmediately(Packet<?> packet);

}
