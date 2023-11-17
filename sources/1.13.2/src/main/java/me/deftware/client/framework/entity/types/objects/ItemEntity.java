package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.entity.item.EntityItem;

/**
 * @author Deftware
 */
public class ItemEntity extends Entity {

	private final ItemStack stack = ItemStack.EMPTY;

	public ItemEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	public ItemStack getStack() {
		return (ItemStack) getMinecraftEntity().getItem();
	}

	public EntityItem getMinecraftEntity() {
		return (EntityItem) entity;
	}

}
