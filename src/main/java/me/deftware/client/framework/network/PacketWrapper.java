package me.deftware.client.framework.network;

import me.deftware.client.framework.network.packets.*;
import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.packet.EntityAnimationS2CPacket;
import net.minecraft.client.network.packet.EntityS2CPacket;
import net.minecraft.network.Packet;
import net.minecraft.server.network.packet.ClientStatusC2SPacket;
import net.minecraft.server.network.packet.GuiCloseC2SPacket;
import net.minecraft.server.network.packet.KeepAliveC2SPacket;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Describes the packet structure with all of it's data
 *
 * @author Deftware
 */
@SuppressWarnings("ConstantConditions")
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
        packet.write(buffer.buffer);
    }

    public void sendPacket() {
        net.minecraft.client.Minecraft.getInstance().player.networkHandler.sendPacket(packet);
    }

    /**
     * Bypasses this event, and can be used to prevent an infinite loop
     */
    public void sendImmediately() {
        ((IMixinNetworkManager) net.minecraft.client.Minecraft.getInstance().player.networkHandler.getClientConnection()).sendPacketImmediately(packet);
    }

    /**
     * Converts a packet into the EMC Packet wrapper
     */
    @Nullable
    public static PacketWrapper translatePacket(Packet<?> packet) {
        // Client to server packets
        if (packet instanceof PlayerMoveC2SPacket) {
            return new CPacketPlayer(packet);
        } else if (packet instanceof PlayerMoveC2SPacket.Both) {
            return new CPacketPositionRotation(packet);
        } else if (packet instanceof PlayerMoveC2SPacket.LookOnly) {
            return new CPacketRotation(packet);
        } else if (packet instanceof PlayerMoveC2SPacket.PositionOnly) {
            return new CPacketPosition(packet);
        } else if (packet instanceof GuiCloseC2SPacket) {
            return new CPacketCloseWindow(packet);
        } else if (packet instanceof KeepAliveC2SPacket) {
            return new CPacketKeepAlive(packet);
        } else if (packet instanceof ClientStatusC2SPacket) {
            return new CPacketClientStatus(packet);
        }
        // Server to client packets
        if (packet instanceof EntityS2CPacket) {
            return new SPacketEntity(packet);
        } else if (packet instanceof EntityAnimationS2CPacket) {
            return new SPacketAnimation(packet);
        }
        return new PacketWrapper(packet);
    }


}
