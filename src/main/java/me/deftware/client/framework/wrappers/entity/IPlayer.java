package me.deftware.client.framework.wrappers.entity;

import me.deftware.client.framework.wrappers.item.IItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class IPlayer extends IEntity {

    private EntityPlayer player;

    public IPlayer(EntityPlayer player) {
        super(player);
        this.player = player;
    }

    public String getUUID() {
        return player.getGameProfile().getId().toString();
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public String getName() {
        return player.getGameProfile().getName();
    }

    public boolean isSelf() {
        return player == Minecraft.getMinecraft().player
                || player.getName().equals(Minecraft.getMinecraft().getSession().getUsername());
    }

    public IItemStack getHeldItem() {
        if (player.inventory.getCurrentItem() != null) {
            return new IItemStack(player.inventory.getCurrentItem());
        }
        return null;
    }

    public boolean isCreative() {
        return player.isCreative();
    }

    public void setGlowing(boolean state) {
        player.setGlowing(state);
    }

}