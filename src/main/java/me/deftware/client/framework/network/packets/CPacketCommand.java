package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;

public class CPacketCommand extends PacketWrapper {

    public CPacketCommand(Packet<?> packet) {
        super(packet);
    }

    public CPacketCommand(Entity entity, Modes mode) {
        super(new CPacketEntityAction(entity.getMinecraftEntity(), mode.getMinecraftMode()));
    }

    public enum Modes {
        PRESS_SHIFT_KEY,
        RELEASE_SHIFT_KEY,
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        START_RIDING_JUMP,
        STOP_RIDING_JUMP,
        OPEN_INVENTORY,
        START_FALL_FLYING;

        CPacketEntityAction.Action getMinecraftMode() {
            return CPacketEntityAction.Action.valueOf(name());
        }
    }

}
