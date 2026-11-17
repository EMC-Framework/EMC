package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.item.ItemStack;

/**
 * @author Deftware
 */
public class ItemEntity extends Entity {

	private final ItemStack stack = ItemStack.EMPTY;

	public ItemEntity(net.minecraft.world.entity.Entity entity) {
		super(entity);
	}

	public ItemStack getStack() {
		return (ItemStack) getMinecraftEntity().getItem();
	}

	public net.minecraft.world.entity.item.ItemEntity getMinecraftEntity() {
		return (net.minecraft.world.entity.item.ItemEntity) entity;
	}

}
