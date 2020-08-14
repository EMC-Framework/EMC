package me.deftware.client.framework.wrappers.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;

@SuppressWarnings("ALL")
public class IEntityOtherPlayerMP extends EntityOtherPlayerMP {

    public IEntityOtherPlayerMP() {
        super(Minecraft.getInstance().world, Minecraft.getInstance().player.getGameProfile());
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
        if (getDataManager() != null) {
            getDataManager().set(EntityPlayer.PLAYER_MODEL_FLAG, oldPlayer.getDataManager().get(EntityPlayer.PLAYER_MODEL_FLAG));
        }
    }

}
