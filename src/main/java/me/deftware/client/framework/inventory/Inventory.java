package me.deftware.client.framework.inventory;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.inventory.InventoryLargeChest;

/**
 * @author Deftware
 */
public interface Inventory {

	int getSize();

	boolean isEmpty();

	ItemStack getStackInSlot(int slotId);

	default int findItem(Item item) {
		for (int i = 0; i < getSize(); i++) {
			ItemStack it = getStackInSlot(i);
			if (it.getItem().equals(item)) {
				return i;
			}
		}
		return -1;
	}

	default boolean isFull() {
		for (int i = 0; i < getSize(); i++)
			if (getStackInSlot(i).isEmpty())
				return false;
		return true;
	}

	default boolean isDouble() {
		return this instanceof InventoryLargeChest;
	}

}
