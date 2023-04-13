package me.deftware.client.framework.network;

import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

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
        Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().sendPacket(packet);
    }

    /**
     * Bypasses this event, and can be used to prevent an infinite loop
     */
    public void sendImmediately() {
        ((IMixinNetworkManager) Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager()).sendPacketImmediately(packet);
    }

}
