package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.conversion.ComparedConversion;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.entity.item.EntityItem;

/**
 * @author Deftware
 */
public class ItemEntity extends Entity {

	private final ComparedConversion<net.minecraft.item.ItemStack, ItemStack> itemStack;

	public ItemEntity(net.minecraft.entity.Entity entity) {
		super(entity);
		this.itemStack = new ComparedConversion<>(() -> getMinecraftEntity().getEntityItem(), ItemStack::new);
	}

	public ItemStack getStack() {
		return itemStack.get();
	}

	public EntityItem getMinecraftEntity() {
		return (EntityItem) entity;
	}

}
