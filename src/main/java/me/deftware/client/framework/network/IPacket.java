package me.deftware.client.framework.network;

import me.deftware.client.framework.network.packets.*;
import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Describes the packet structure with all of it's data
 */
@SuppressWarnings("ConstantConditions")
public class IPacket {

    protected Packet<?> packet;

    private IPacketBuffer packetBuffer;

    public IPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public IPacket() {
        this.packet = null;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public IPacketBuffer getPacketBuffer() {
        return packetBuffer;
    }

    public void setPacketBuffer(IPacketBuffer buffer) throws IOException {
        packet.writePacketData(buffer.buffer);
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public void sendPacket() {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
    }

    /**
     * Bypasses this event, and can be used to prevent an infinite loop
     */
    public void sendImmediately() {
        ((IMixinNetworkManager) Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager()).sendPacketImmediately(packet);
    }

    /**
     * Converts a packet into the EMC IPacket wrapper
     */
    @Nullable
    public static IPacket translatePacket(Packet<?> packet) {
        // Client to server packets
        if (packet instanceof C03PacketPlayer) {
            return new ICPacketPlayer(packet);
        } else if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            return new ICPacketPositionRotation(packet);
        } else if (packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
            return new ICPacketRotation(packet);
        } else if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            return new ICPacketPosition(packet);
        } else if (packet instanceof C0DPacketCloseWindow) {
            return new ICPacketCloseWindow(packet);
        } else if (packet instanceof C00PacketKeepAlive) {
            return new ICPacketKeepAlive(packet);
        } else if (packet instanceof C16PacketClientStatus) {
            return new ICPacketClientStatus(packet);
        }
        // Server to client packets
        if (packet instanceof S14PacketEntity) {
            return new ISPacketEntity(packet);
        } else if (packet instanceof S0BPacketAnimation) {
            return new ISPacketAnimation(packet);
        }
        return new IPacket(packet);
    }


}
