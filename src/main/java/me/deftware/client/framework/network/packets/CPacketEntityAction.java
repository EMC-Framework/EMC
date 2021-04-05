package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.Packet;

public class CPacketEntityAction extends PacketWrapper {
    
    public CPacketEntityAction(Packet<?> packet) {
        super(packet);
    }
    
    public Action getAction() {
        switch (((net.minecraft.network.play.client.C0BPacketEntityAction)packet).getAction()) {
            case START_SNEAKING:
                return Action.START_SNEAK;
            case STOP_SNEAKING:
                return Action.STOP_SNEAK;
            case STOP_SLEEPING:
                return Action.STOP_SLEEP;
            case START_SPRINTING:
                return Action.START_SPRINT;
            case STOP_SPRINTING:
                return Action.STOP_SPRINT;
            case RIDING_JUMP:
                return Action.STOP_HORSE_JUMP;
            case OPEN_INVENTORY:
                return Action.OPEN_INVENTORY;
            default:
                return null;
        }
    }
    
    public enum Action {
        START_SNEAK, STOP_SNEAK, STOP_SLEEP, START_SPRINT, STOP_SPRINT, START_HORSE_JUMP, STOP_HORSE_JUMP, OPEN_INVENTORY, START_GLIDING
    }
}
