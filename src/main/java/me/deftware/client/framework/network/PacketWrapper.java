package me.deftware.client.framework.network;

import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import java.io.IOException;

/**
 * Describes the packet structure with all of it's data
 *
 * @author Deftware
 */
public class PacketWrapper {

    protected Packet<?> packet;
    private PacketBuffer packetBuffer;

    public PacketWrapper(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public PacketBuffer getPacketBuffer() {
        if (packetBuffer == null)
            packetBuffer = new PacketBuffer();
        return packetBuffer;
    }

    public void setPacketBuffer(PacketBuffer buffer) throws IOException {
        packet.writePacketData(buffer.buffer);
    }

    public void sendPacket() {
        Minecraft.getMinecraft().player.connection.sendPacket(packet);
    }

    /**
     * Bypasses this event, and can be used to prevent an infinite loop
     */
    public void sendImmediately() {
        ((IMixinNetworkManager) Minecraft.getMinecraft().player.connection.getNetworkManager()).sendPacketImmediately(packet);
    }

}
