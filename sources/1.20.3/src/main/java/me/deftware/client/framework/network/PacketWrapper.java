package me.deftware.client.framework.network;

import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;

/**
 * Describes the packet structure with all of it's data
 *
 * @author Deftware
 */
public class PacketWrapper {

    protected Packet<?> packet;

    public PacketWrapper(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void sendPacket() {
        MinecraftClient.getInstance().player.networkHandler.sendPacket(packet);
    }

    /**
     * Bypasses this event, and can be used to prevent an infinite loop
     */
    public void sendImmediately() {
        ((IMixinNetworkManager) MinecraftClient.getInstance().player.networkHandler.getConnection()).sendPacketImmediately(packet);
    }

}
