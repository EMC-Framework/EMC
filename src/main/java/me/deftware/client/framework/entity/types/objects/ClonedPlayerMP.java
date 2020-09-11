package me.deftware.client.framework.entity.types.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

/**
 * @author Deftware
 */
@SuppressWarnings("EntityConstructor")
public class ClonedPlayerMP extends EntityOtherPlayerMP {

	public ClonedPlayerMP(EntityPlayer entity) {
		super(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world), entity.getGameProfile());
		clonePlayer(Minecraft.getInstance().player, true);
		copyLocationAndAnglesFrom(Minecraft.getInstance().player);
		rotationYawHead = Minecraft.getInstance().player.rotationYawHead;
	}

	public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd) {
		if (respawnFromEnd) {
			inventory.copyInventory(oldPlayer.inventory);
			setHealth(oldPlayer.getHealth());
			foodStats = oldPlayer.getFoodStats();
			experienceLevel = oldPlayer.experienceLevel;
			experienceTotal = oldPlayer.experienceTotal;
			experience = oldPlayer.experience;
			setScore(oldPlayer.getScore());
			lastPortalVec = oldPlayer.getLastPortalVec();
			teleportDirection = oldPlayer.getTeleportDirection();
		} else if (world.getGameRules().getBoolean("keepInventory") || oldPlayer.isSpectator()) {
			inventory.copyInventory(oldPlayer.inventory);
			experienceLevel = oldPlayer.experienceLevel;
			experienceTotal = oldPlayer.experienceTotal;
			experience = oldPlayer.experience;
			setScore(oldPlayer.getScore());
		}
		xpSeed = oldPlayer.getXPSeed();
		getDataManager().set(EntityPlayer.PLAYER_MODEL_FLAG, oldPlayer.getDataManager().get(EntityPlayer.PLAYER_MODEL_FLAG));
	}

}

