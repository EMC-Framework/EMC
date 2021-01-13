package me.deftware.client.framework.network;

import me.deftware.client.framework.network.packets.*;
import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import javax.annotation.Nullable;
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
        return packetBuffer;
    }

    public void setPacketBuffer(PacketBuffer buffer) throws IOException {
        packet.writePacketData(buffer.buffer);
    }

    public void sendPacket() {
        Minecraft.getInstance().player.connection.sendPacket(packet);
    }

    /**
     * Bypasses this event, and can be used to prevent an infinite loop
     */
    public void sendImmediately() {
        ((IMixinNetworkManager) Minecraft.getInstance().player.connection.getNetworkManager()).sendPacketImmediately(packet);
    }

    /**
     * Converts a packet into the EMC Packet wrapper
     */
    public static PacketWrapper translatePacket(Packet<?> packet) {
        // Client to server packets
        if (packet instanceof net.minecraft.network.play.client.CPacketUseEntity) {
            return new CPacketUseEntity(packet);
        } else if (packet instanceof net.minecraft.network.play.client.CPacketPlayer) {
            return new CPacketPlayer(packet);
        } else if (packet instanceof net.minecraft.network.play.client.CPacketCloseWindow) {
            return new CPacketCloseWindow(packet);
        } else if (packet instanceof net.minecraft.network.play.client.CPacketKeepAlive) {
            return new CPacketKeepAlive(packet);
        } else if (packet instanceof net.minecraft.network.play.client.CPacketClientStatus) {
            return new CPacketClientStatus(packet);
        }
        // Server to client packets
        if (packet instanceof net.minecraft.network.play.server.SPacketEntity) {
            return new SPacketEntity(packet);
        } else if (packet instanceof net.minecraft.network.play.server.SPacketAnimation) {
            return new SPacketAnimation(packet);
        }
        return new PacketWrapper(packet);
    }


}
