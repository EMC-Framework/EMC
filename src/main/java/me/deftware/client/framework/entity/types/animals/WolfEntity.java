package me.deftware.client.framework.entity.types.animals;

import me.deftware.client.framework.entity.types.EntityPlayer;
import me.deftware.client.framework.entity.types.OwnedEntity;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author Deftware
 */
public class WolfEntity extends OwnedEntity {

	public WolfEntity(net.minecraft.world.entity.Entity entity) {
		super(entity);
	}

	public net.minecraft.world.entity.animal.wolf.Wolf getMinecraftEntity() {
		return (net.minecraft.world.entity.animal.wolf.Wolf) entity;
	}

	public boolean isPlayerOwned(EntityPlayer player) {
		return getMinecraftEntity().isOwnedBy(player.getMinecraftEntity());
	}

	public String getOwnerName(boolean displayName) {
		LivingEntity entity = getMinecraftEntity().getOwner();
		if  (entity != null) {
			return (displayName ? entity.getDisplayName() : entity.getName()).getString();
		}
		return "";
	}

	public String getEntityName(boolean displayName) {
		return (displayName ? getMinecraftEntity().getDisplayName() : getMinecraftEntity().getName()).getString();
	}

}
