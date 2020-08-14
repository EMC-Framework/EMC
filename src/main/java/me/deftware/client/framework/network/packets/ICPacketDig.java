package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.IPacket;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import me.deftware.client.framework.wrappers.world.IEnumFacing;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;

public class ICPacketDig extends IPacket {

    public ICPacketDig(Packet<?> packet) {
        super(packet);
    }

    public ICPacketDig(IDigAction action, IBlockPos pos, IEnumFacing facing) {
        super(new CPacketPlayerDigging(getAction(action), pos.getPos(), IEnumFacing.getFacing(facing)));
    }

    public static CPacketPlayerDigging.Action getAction(IDigAction action) {
        if (action.equals(IDigAction.START_DESTROY_BLOCK)) {
            return CPacketPlayerDigging.Action.START_DESTROY_BLOCK;
        } else if (action.equals(IDigAction.STOP_DESTROY_BLOCK)) {
            return CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK;
        }
        return null;
    }

    public enum IDigAction {
        START_DESTROY_BLOCK, STOP_DESTROY_BLOCK
    }

}