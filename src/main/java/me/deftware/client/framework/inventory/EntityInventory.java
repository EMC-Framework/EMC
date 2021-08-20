package me.deftware.client.framework.inventory;

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
		ItemStack.copyReferences(entity.inventory.armorInventory, armor);
		return armor;
	}

	public List<ItemStack> getMainInventory() {
		ItemStack.copyReferences(entity.inventory.mainInventory, main);
		return main;
	}

	public int getCurrentItem() {
		return entity.inventory.currentItem;
	}

	public void setCurrentItem(int id) {
		entity.inventory.currentItem = id;
	}

	public ItemStack getHeldItem(boolean offhand) {
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
