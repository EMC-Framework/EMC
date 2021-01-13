package me.deftware.client.framework.network;

import me.deftware.client.framework.network.packets.*;
import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;

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
        Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().sendPacket(packet);
    }

    /**
     * Bypasses this event, and can be used to prevent an infinite loop
     */
    public void sendImmediately() {
        ((IMixinNetworkManager) Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager()).sendPacketImmediately(packet);
    }

    /**
     * Converts a packet into the EMC Packet wrapper
     */
    public static PacketWrapper translatePacket(Packet<?> packet) {
        // Client to server packets
        if (packet instanceof C02PacketUseEntity) {
            return new CPacketUseEntity(packet);
        } else if (packet instanceof C03PacketPlayer) {
            return new CPacketPlayer(packet);
        } else if (packet instanceof C0DPacketCloseWindow) {
            return new CPacketCloseWindow(packet);
        } else if (packet instanceof C00PacketKeepAlive) {
            return new CPacketKeepAlive(packet);
        } else if (packet instanceof C16PacketClientStatus) {
            return new CPacketClientStatus(packet);
        }
        // Server to client packets
        if (packet instanceof S14PacketEntity) {
            return new SPacketEntity(packet);
        } else if (packet instanceof S0BPacketAnimation) {
            return new SPacketAnimation(packet);
        }
        return new PacketWrapper(packet);
    }


}
