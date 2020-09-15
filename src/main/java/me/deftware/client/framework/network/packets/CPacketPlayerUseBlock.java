package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

/**
 * @author Deftware
 */
public class CPacketPlayerUseBlock extends PacketWrapper {

	public CPacketPlayerUseBlock(Packet<?> packet) {
		super(packet);
	}

	public CPacketPlayerUseBlock(BlockSwingResult swingResult) {
		/*this(new CPacketPlayerTryUseItem( FIXME
				swingResult.getBlockPos(), swingResult.sideHit, EnumHand.MAIN_HAND,
				(float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ
		));*/
		super(null);
	}
}
