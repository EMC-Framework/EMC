package me.deftware.client.framework.entity.types.objects;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.Player;

/**
 * @author Deftware
 */
public class ClonedPlayerMP extends RemotePlayer {

	public ClonedPlayerMP(Player entity) {
		super(Objects.requireNonNull(Minecraft.getInstance().level), entity.getGameProfile());
		clonePlayer(entity, true);
		snapTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
		yHeadRot = entity.yHeadRot;
	}

	public void clonePlayer(Player oldPlayer, boolean respawnFromEnd) {
		if (respawnFromEnd) {
			getInventory().replaceWith(oldPlayer.getInventory());
			setHealth(oldPlayer.getHealth());
			foodData = oldPlayer.getFoodData();
			experienceLevel = oldPlayer.experienceLevel;
			experienceProgress = oldPlayer.experienceProgress;
			totalExperience = oldPlayer.totalExperience;
			setScore(oldPlayer.getScore());
			restoreFrom(oldPlayer);
		} else if (oldPlayer.isSpectator()) { // getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)
			getInventory().replaceWith(oldPlayer.getInventory());
			experienceLevel = oldPlayer.experienceLevel;
			experienceProgress = oldPlayer.experienceProgress;
			totalExperience = oldPlayer.totalExperience;
			setScore(oldPlayer.getScore());
		}
		enchantmentSeed = oldPlayer.getEnchantmentSeed();
		if (getEntityData() != null) {
			getEntityData().set(Player.DATA_PLAYER_MODE_CUSTOMISATION, oldPlayer.getEntityData().get(Player.DATA_PLAYER_MODE_CUSTOMISATION));
		}
	}

}

