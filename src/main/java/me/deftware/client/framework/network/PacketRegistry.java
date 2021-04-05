package me.deftware.client.framework.network;

import me.deftware.client.framework.network.packets.*;
import net.minecraft.network.Packet;

import java.util.HashMap;

/**
 * Responsible for translating Minecraft packets to EMC wrappers
 *
 * @author Deftware
 */
public class PacketRegistry {

    public static final PacketRegistry INSTANCE = new PacketRegistry();

    private final HashMap<Class<? extends Packet<?>>, Class<? extends PacketWrapper>> packetMap = new HashMap<>();

    private PacketRegistry() {
        register(net.minecraft.network.play.client.CPacketUseEntity.class, CPacketUseEntity.class);
        register(net.minecraft.network.play.client.CPacketCloseWindow.class, CPacketCloseWindow.class);
        register(net.minecraft.network.play.client.CPacketKeepAlive.class, CPacketKeepAlive.class);
        register(net.minecraft.network.play.client.CPacketClientStatus.class, CPacketClientStatus.class);
        // Move packets
        register(net.minecraft.network.play.client.CPacketPlayer.class, CPacketPlayer.class);
        register(net.minecraft.network.play.client.CPacketPlayer.PositionRotation.class, CPacketPositionRotation.class);
        register(net.minecraft.network.play.client.CPacketPlayer.Rotation.class, CPacketRotation.class);
        register(net.minecraft.network.play.client.CPacketPlayer.Position.class, CPacketPosition.class);
        // Player Actions
        register(net.minecraft.network.play.client.CPacketEntityAction.class, CPacketEntityAction.class);
        // Server bound
        register(net.minecraft.network.play.server.SPacketEntity.class, SPacketEntity.class);
        register(net.minecraft.network.play.server.SPacketAnimation.class, SPacketAnimation.class);
    }

    public void register(Class<? extends Packet<?>> minecraft, Class<? extends PacketWrapper> translated) {
        packetMap.putIfAbsent(minecraft, translated);
    }

    public PacketWrapper translate(Packet<?> packet) {
        if (packetMap.containsKey(packet.getClass())) {
            Class<? extends PacketWrapper> wrapper = packetMap.get(packet.getClass());
            try {
                return wrapper.getDeclaredConstructor(Packet.class).newInstance(packet);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new PacketWrapper(packet);
    }

}
