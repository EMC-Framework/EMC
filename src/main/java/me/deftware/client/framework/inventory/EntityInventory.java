package me.deftware.client.framework.inventory;

import me.deftware.client.framework.item.ItemStack;

import java.util.function.Consumer;

/**
 * @author Deftware
 */
public interface EntityInventory extends Inventory {

	void armor(Consumer<ItemStack> consumer);

	void main(Consumer<ItemStack> consumer);

	ItemStack getStackInArmourSlot(int slotId);

	void setCurrentItem(int id);

	int getCurrentItem();

}
