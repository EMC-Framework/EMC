package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.client.framework.world.EnumFacing;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;

/**
 * @author Deftware
 */
public class CPacketDig extends PacketWrapper {

    public static volatile long lastStopDigTimeMs = 0L;

    public CPacketDig(Packet<?> packet) {
        super(packet);
    }

    public CPacketDig(IDigAction action, BlockPosition pos, EnumFacing facing) {
        super(new ServerboundPlayerActionPacket(getAction(action), (BlockPos) pos, facing.getFacing()));
        if (action == IDigAction.STOP_DESTROY_BLOCK) {
            lastStopDigTimeMs = System.currentTimeMillis();
        }
    }

    public static ServerboundPlayerActionPacket.Action getAction(IDigAction action) {
        if (action.equals(IDigAction.START_DESTROY_BLOCK)) {
            return ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK;
        } else if (action.equals(IDigAction.STOP_DESTROY_BLOCK)) {
            return ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK;
        }
        return null;
    }

    public enum IDigAction {
        START_DESTROY_BLOCK, STOP_DESTROY_BLOCK
    }

}