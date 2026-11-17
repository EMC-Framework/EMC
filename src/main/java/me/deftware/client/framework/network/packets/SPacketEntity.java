package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.network.PacketWrapper;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import javax.annotation.Nullable;

/**
 * @author Deftware
 */
public class SPacketEntity extends PacketWrapper {

	public SPacketEntity(Packet<?> packet) {
		super(packet);
	}

	public boolean isOnGround() {
		return ((ClientboundMoveEntityPacket) packet).isOnGround();
	}

	@Nullable
	public Entity getEntity() {
		return ClientWorld.getClientWorld().getEntityByReference(
				((ClientboundMoveEntityPacket) packet).getEntity(Minecraft.getInstance().level)
		);
	}

}
