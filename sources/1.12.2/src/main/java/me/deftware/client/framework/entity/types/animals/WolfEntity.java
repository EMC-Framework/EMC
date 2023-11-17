package me.deftware.client.framework.entity.types.animals;

import me.deftware.client.framework.entity.types.EntityPlayer;
import me.deftware.client.framework.entity.types.OwnedEntity;
import net.minecraft.entity.passive.EntityWolf;

/**
 * @author Deftware
 */
public class WolfEntity extends OwnedEntity {

	public WolfEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	public EntityWolf getMinecraftEntity() {
		return (EntityWolf) entity;
	}

	public boolean isPlayerOwned(EntityPlayer player) {
		return getMinecraftEntity().isOwner(player.getMinecraftEntity());
	}

	public String getOwnerName(boolean displayName) {
		return getMinecraftEntity().getOwner().getName();
	}

	public String getEntityName(boolean displayName) {
		return getMinecraftEntity().getName();
	}

}
