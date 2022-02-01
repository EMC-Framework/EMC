package me.deftware.client.framework.inventory;

import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.util.Util;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;

/**
 * @author Deftware
 */
public class EntityInventory extends Inventory {

	protected final List<ItemStack> main = Util.getEmptyStackList(36), armor = Util.getEmptyStackList(4);

	private final EntityPlayer entity;

	public EntityInventory(EntityPlayer entity) {
		super(entity.inventory);
		this.delegate.clear();
		this.entity = entity;
		ItemStack.init(Arrays.asList(entity.inventory.mainInventory), main);
		ItemStack.init(Arrays.asList(entity.inventory.armorInventory), armor);
		// Add items to delegate
		this.delegate.addAll(main);
		this.delegate.addAll(armor);
	}

	public List<ItemStack> getArmourInventory() {
		int index = 0;
		for (net.minecraft.item.ItemStack item : entity.inventory.armorInventory)
			armor.get(index++).setStack(item);
		return armor;
	}

	public List<ItemStack> getMainInventory() {
		int index = 0;
		for (net.minecraft.item.ItemStack item : entity.inventory.mainInventory)
			main.get(index++).setStack(item);
		return main;
	}

	public int getCurrentItem() {
		return entity.inventory.currentItem;
	}

	public void setCurrentItem(int id) {
		entity.inventory.currentItem = id;
	}

	@Deprecated
	public ItemStack getHeldItem(boolean offhand) {
		return this.getHeldItem(EntityHand.MainHand);
	}

	public ItemStack getHeldItem(EntityHand hand) {
		return main.get(getCurrentItem()).setStack(entity.inventory.getStackInSlot(getCurrentItem()));
	}

	public ItemStack getStackInArmourSlot(int slotId) {
		if (slotId >= armor.size())
			return ItemStack.EMPTY;
		return armor.get(slotId).setStack(entity.inventory.armorInventory[slotId]);
	}

	public boolean hasElytra() {
		return false;
	}

}
