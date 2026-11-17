package me.deftware.client.framework.network;

import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.network.packets.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import java.util.HashMap;

/**
 * Responsible for translating Minecraft packets to EMC wrappers
 *
 * @author Deftware
 */
public class PacketRegistry {

    public static final PacketRegistry INSTANCE = new PacketRegistry();

    @Getter
    @Setter
    private SocksProxy proxy;

    private final HashMap<Class<? extends Packet<?>>, Class<? extends PacketWrapper>> packetMap = new HashMap<>();

    private PacketRegistry() {
        register(net.minecraft.network.protocol.game.ServerboundAttackPacket.class, CPacketUseEntity.class);
        register(ServerboundInteractPacket.class, CPacketUseEntity.class);
        register(ServerboundContainerClosePacket.class, CPacketCloseWindow.class);
        register(ServerboundKeepAlivePacket.class, CPacketKeepAlive.class);
        register(ServerboundClientCommandPacket.class, CPacketClientStatus.class);
        register(ServerboundChatPacket.class, CPacketChatMessage.class);
        // Move packets
        register(ServerboundMovePlayerPacket.class, CPacketPlayer.class);
        register(ServerboundMovePlayerPacket.StatusOnly.class, CPacketPlayer.class);
        register(ServerboundMovePlayerPacket.PosRot.class, CPacketPositionRotation.class);
        register(ServerboundMovePlayerPacket.Rot.class, CPacketRotation.class);
        register(ServerboundMovePlayerPacket.Pos.class, CPacketPosition.class);
        // Player Actions
        register(ServerboundPlayerCommandPacket.class, CPacketEntityAction.class);
        // Server bound
        register(ClientboundMoveEntityPacket.class, SPacketEntity.class);
        register(ClientboundAnimatePacket.class, SPacketAnimation.class);
        register(ClientboundSetTimePacket.class, SPacketWorldTime.class);
        register(ClientboundOpenScreenPacket.class, SPacketOpenScreen.class);
        register(ClientboundMerchantOffersPacket.class, SPacketTradeOffers.class);
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
