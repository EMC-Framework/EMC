package me.deftware.client.framework.inventory;

import me.deftware.client.framework.conversion.ComparedConversion;
import me.deftware.client.framework.conversion.ConvertedList;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @author Deftware
 */
public class Inventory {

	protected final ConvertedList<ItemStack, net.minecraft.item.ItemStack> armourInventory, mainInventory;
	protected final ComparedConversion<net.minecraft.item.ItemStack, ItemStack> mainHand;
	protected final net.minecraft.entity.player.EntityPlayer entity;

	public Inventory(net.minecraft.entity.player.EntityPlayer entity) {
		this.entity = entity;
		this.mainHand = new ComparedConversion<>(entity::getHeldItem, ItemStack::new);

		this.armourInventory = new ConvertedList<>(() -> Arrays.asList(entity.inventory.armorInventory),pair ->
			pair.getLeft().getMinecraftItemStack() == entity.inventory.armorInventory[pair.getRight()]
		, ItemStack::new);

		this.mainInventory = new ConvertedList<>(() -> Arrays.asList(entity.inventory.mainInventory), pair ->
				net.minecraft.item.ItemStack.areItemsEqual(pair.getLeft().getMinecraftItemStack(), entity.inventory.mainInventory[pair.getRight()])
				, ItemStack::new);
	}

	public int findItem(Item item) {
		for (int i = 0; i < entity.inventory.mainInventory.length; i++) {
			net.minecraft.item.ItemStack it = entity.inventory.mainInventory[i];
			if (it.getItem().getUnlocalizedName().equals(item.getUnlocalizedName())) {
				return i;
			}
		}
		return -1;
	}

	public int getSize() {
		return entity.inventory.mainInventory.length;
	}

	public List<ItemStack> getArmourInventory() {
		return armourInventory.poll();
	}

	public List<ItemStack> getMainInventory() {
		return mainInventory.poll();
	}
	
	public int getFirstEmptyStack() {
		return entity.inventory.getFirstEmptyStack();
	}

	public int getCurrentItem() {
		return entity.inventory.currentItem;
	}

	public int getFirstEmptySlot() {
		return entity.inventory.getFirstEmptyStack();
	}

	public void setCurrentItem(int id) {
		entity.inventory.currentItem = id;
	}

	public ItemStack getHeldItem(boolean offhand) {
		ItemStack stack = offhand ? null : this.mainHand.get();
		if (stack == null) stack = ItemStack.EMPTY;
		return stack;
	}

	public ItemStack getStackInSlot(int slotId) {
		return new ItemStack(entity.inventory.getStackInSlot(slotId));
	}

	public ItemStack getStackInArmourSlot(int slotId) {
		return new ItemStack(entity.inventory.armorItemInSlot(slotId));
	}

	public boolean hasElytra() {
		return false;
	}

}
