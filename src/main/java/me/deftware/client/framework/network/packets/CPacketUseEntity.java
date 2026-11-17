package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.mixin.imp.IMixinPlayerInteractEntityC2SPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;

/**
 * @author Deftware
 */
public class CPacketUseEntity extends PacketWrapper {

    public CPacketUseEntity(Packet<?> packet) {
        super(packet);
    }

    public static CPacketUseEntity attack(Entity entity) {
        return new CPacketUseEntity(new net.minecraft.network.protocol.game.ServerboundAttackPacket(entity.getMinecraftEntity().getId()));
    }

    public static CPacketUseEntity interact(Entity entity) {
        return new CPacketUseEntity(new ServerboundInteractPacket(entity.getMinecraftEntity().getId(), InteractionHand.MAIN_HAND, net.minecraft.world.phys.Vec3.ZERO, entity.getMinecraftEntity().isShiftKeyDown()));
    }

    public Type getType() {
        if (packet instanceof net.minecraft.network.protocol.game.ServerboundAttackPacket) {
            return Type.ATTACK;
        }
        Type type = ((IMixinPlayerInteractEntityC2SPacket) packet).getActionType();
        if (type == null)
            return Type.UNKNOWN;
        return switch (type) {
            case ATTACK -> Type.ATTACK;
            case INTERACT -> Type.INTERACT;
            case INTERACT_AT -> Type.INTERACT_AT;
            default -> Type.UNKNOWN;
        };
    }

    public enum Type {
        ATTACK, INTERACT, INTERACT_AT, UNKNOWN
    }

}
