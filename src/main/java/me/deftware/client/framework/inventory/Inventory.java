package me.deftware.client.framework.inventory;

import me.deftware.client.framework.conversion.ComparedConversion;
import me.deftware.client.framework.conversion.ConvertedList;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.List;

/**
 * @author Deftware
 */
public class Inventory {

	protected final ConvertedList<ItemStack, net.minecraft.item.ItemStack> armourInventory, mainInventory;
	protected final ComparedConversion<net.minecraft.item.ItemStack, ItemStack> mainHand, offHand;
	protected final net.minecraft.entity.player.EntityPlayer entity;

	public Inventory(net.minecraft.entity.player.EntityPlayer entity) {
		this.entity = entity;
		this.mainHand = new ComparedConversion<>(entity::getHeldItemMainhand, ItemStack::new);
		this.offHand = new ComparedConversion<>(entity::getHeldItemOffhand, ItemStack::new);

		this.armourInventory = new ConvertedList<>(() -> entity.inventory.armorInventory, pair ->
			pair.getLeft().getMinecraftItemStack() == entity.inventory.armorInventory.get(pair.getRight())
		, ItemStack::new);

		this.mainInventory = new ConvertedList<>(() -> entity.inventory.mainInventory, pair ->
				net.minecraft.item.ItemStack.areItemsEqual(pair.getLeft().getMinecraftItemStack(), entity.inventory.mainInventory.get(pair.getRight()))
				, ItemStack::new);
	}

	public int findItem(Item item) {
		for (int i = 0; i < entity.inventory.mainInventory.size(); i++) {
			net.minecraft.item.ItemStack it = entity.inventory.mainInventory.get(i);
			if (it.getItem().getTranslationKey().equals(item.getTranslationKey())) {
				return i;
			}
		}
		return -1;
	}

	public int getSize() {
		return entity.inventory.mainInventory.size();
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
		return offhand ? this.offHand.get() : this.mainHand.get();
	}

	public ItemStack getStackInSlot(int slotId) {
		return new ItemStack(entity.inventory.getStackInSlot(slotId));
	}

	public ItemStack getStackInArmourSlot(int slotId) {
		return new ItemStack(entity.inventory.armorItemInSlot(slotId));
	}

	public boolean hasElytra() {
		net.minecraft.item.ItemStack chest = net.minecraft.client.Minecraft.getInstance().player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		return chest.getItem() == Items.ELYTRA;
	}

}
