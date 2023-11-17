package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.client.framework.world.EnumFacing;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;

/**
 * @author Deftware
 */
public class CPacketDig extends PacketWrapper {

    public CPacketDig(Packet<?> packet) {
        super(packet);
    }

    public CPacketDig(IDigAction action, BlockPosition pos, EnumFacing facing) {
        super(new C07PacketPlayerDigging(getAction(action), (BlockPos) pos, facing.getFacing()));
    }

    public static C07PacketPlayerDigging.Action getAction(IDigAction action) {
        if (action.equals(IDigAction.START_DESTROY_BLOCK)) {
            return C07PacketPlayerDigging.Action.START_DESTROY_BLOCK;
        } else if (action.equals(IDigAction.STOP_DESTROY_BLOCK)) {
            return C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK;
        }
        return null;
    }

    public enum IDigAction {
        START_DESTROY_BLOCK, STOP_DESTROY_BLOCK
    }


}