package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;

/**
 * @author Deftware
 */
public class CPacketPlayerUseBlock extends PacketWrapper {

	public CPacketPlayerUseBlock(Packet<?> packet) {
		super(packet);
	}

	public CPacketPlayerUseBlock(BlockSwingResult swingResult) {
		this(new CPacketPlayerTryUseItemOnBlock(
				swingResult.getBlockPos(), swingResult.sideHit, EnumHand.MAIN_HAND,
				(float) Minecraft.getMinecraft().player.posX, (float) Minecraft.getMinecraft().player.posY, (float) Minecraft.getMinecraft().player.posZ
		));
	}
}
