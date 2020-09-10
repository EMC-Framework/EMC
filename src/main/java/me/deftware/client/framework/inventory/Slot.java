package me.deftware.client.framework.inventory;

import me.deftware.client.framework.item.ItemStack;

/**
 * @author Deftware
 */
public class Slot {

	protected final net.minecraft.container.Slot slot;
	private ItemStack cachedItemStack;

	public Slot(net.minecraft.container.Slot slot) {
		this.slot = slot;
		this.cachedItemStack = new ItemStack(slot.getStack());
	}

	public net.minecraft.container.Slot getMinecraftSlot() {
		return slot;
	}

	public int getId() {
		return slot.id;
	}

	public ItemStack getStack() {
		if (cachedItemStack.getMinecraftItemStack() != slot.getStack()) {
			this.cachedItemStack = new ItemStack(slot.getStack());
		}
		return cachedItemStack;
	}

}
