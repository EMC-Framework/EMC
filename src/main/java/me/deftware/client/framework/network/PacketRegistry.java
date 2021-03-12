package me.deftware.client.framework.network;

import me.deftware.client.framework.network.packets.*;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.*;

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
        register(PlayerInteractEntityC2SPacket.class, CPacketUseEntity.class);
        register(GuiCloseC2SPacket.class, CPacketCloseWindow.class);
        register(KeepAliveC2SPacket.class, CPacketKeepAlive.class);
        register(ClientStatusC2SPacket.class, CPacketClientStatus.class);
        // Move packets
        register(PlayerMoveC2SPacket.class, CPacketPlayer.class);
        register(PlayerMoveC2SPacket.Both.class, CPacketPositionRotation.class);
        register(PlayerMoveC2SPacket.LookOnly.class, CPacketRotation.class);
        register(PlayerMoveC2SPacket.PositionOnly.class, CPacketPosition.class);
        // Server bound
        register(EntityS2CPacket.class, SPacketEntity.class);
        register(EntityAnimationS2CPacket.class, SPacketAnimation.class);
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
