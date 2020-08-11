package me.deftware.client.framework.network;

import me.deftware.client.framework.network.packets.*;
import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketEntity;

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
        Minecraft.getMinecraft().thePlayer.connection.sendPacket(packet);
    }

    /**
     * Bypasses this event, and can be used to prevent an infinite loop
     */
    public void sendImmediately() {
        ((IMixinNetworkManager) Minecraft.getMinecraft().thePlayer.connection.getNetworkManager()).sendPacketImmediately(packet);
    }

    /**
     * Converts a packet into the EMC IPacket wrapper
     */
    @Nullable
    public static IPacket translatePacket(Packet<?> packet) {
        // Client to server packets
        if (packet instanceof CPacketPlayer) {
            return new ICPacketPlayer(packet);
        } else if (packet instanceof CPacketPlayer.PositionRotation) {
            return new ICPacketPositionRotation(packet);
        } else if (packet instanceof CPacketPlayer.Rotation) {
            return new ICPacketRotation(packet);
        } else if (packet instanceof CPacketPlayer.Position) {
            return new ICPacketPosition(packet);
        } else if (packet instanceof CPacketCloseWindow) {
            return new ICPacketCloseWindow(packet);
        } else if (packet instanceof CPacketKeepAlive) {
            return new ICPacketKeepAlive(packet);
        } else if (packet instanceof CPacketClientStatus) {
            return new ICPacketClientStatus(packet);
        }
        // Server to client packets
        if (packet instanceof SPacketEntity) {
            return new ISPacketEntity(packet);
        } else if (packet instanceof SPacketAnimation) {
            return new ISPacketAnimation(packet);
        }
        return new IPacket(packet);
    }


}
