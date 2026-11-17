package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;

/**
 * @author Deftware
 */
public class CPacketPlayerUseBlock extends PacketWrapper {

	public CPacketPlayerUseBlock(Packet<?> packet) {
		super(packet);
	}

	public CPacketPlayerUseBlock(BlockSwingResult swingResult) {
		// TODO: What is sequence?
		this(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, swingResult.getMinecraftHitResult(), 0));
	}

}
