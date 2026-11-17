package me.deftware.mixin.imp;

import net.minecraft.network.protocol.Packet;

public interface IMixinNetworkManager {

	void sendPacketImmediately(Packet<?> packet);

}
