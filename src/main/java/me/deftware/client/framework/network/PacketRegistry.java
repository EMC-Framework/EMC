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
        register(net.minecraft.network.play.client.C02PacketUseEntity.class, CPacketUseEntity.class);
        register(net.minecraft.network.play.client.C0DPacketCloseWindow.class, CPacketCloseWindow.class);
        register(net.minecraft.network.play.client.C00PacketKeepAlive.class, CPacketKeepAlive.class);
        register(net.minecraft.network.play.client.C16PacketClientStatus.class, CPacketClientStatus.class);
        // Move packets
        register(net.minecraft.network.play.client.C03PacketPlayer.class, CPacketPlayer.class);
        register(net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook.class, CPacketPositionRotation.class);
        register(net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook.class, CPacketRotation.class);
        register(net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition.class, CPacketPosition.class);
        // Player Actions
        register(net.minecraft.network.play.client.C0BPacketEntityAction.class, CPacketEntityAction.class);
        // Server bound
        register(net.minecraft.network.play.server.S14PacketEntity.class, SPacketEntity.class);
        register(net.minecraft.network.play.server.S0BPacketAnimation.class, SPacketAnimation.class);
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
