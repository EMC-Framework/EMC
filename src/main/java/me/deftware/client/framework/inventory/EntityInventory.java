package me.deftware.client.framework.inventory;

import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.List;

/**
 * @author Deftware
 */
public class EntityInventory extends Inventory {

	protected final List<ItemStack> main = Util.getEmptyStackList(36), armor = Util.getEmptyStackList(4);
	protected ItemStack offhand;

	private final EntityPlayer entity;

	public EntityInventory(EntityPlayer entity) {
		super(entity.inventory);
		this.delegate.clear();
		this.entity = entity;
		ItemStack.init(entity.inventory.mainInventory, main);
		ItemStack.init(entity.inventory.armorInventory, armor);
		offhand = new ItemStack(entity.getHeldItemOffhand());
		// Add items to delegate
		this.delegate.addAll(main);
		this.delegate.addAll(armor);
		this.delegate.add(offhand);
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

	@Deprecated
	public ItemStack getHeldItem(boolean offhand) {
		return this.getHeldItem(offhand ? EntityHand.OffHand : EntityHand.MainHand);
	}

	public ItemStack getHeldItem(EntityHand hand) {
		if (hand == EntityHand.OffHand)
			return this.offhand.setStack(entity.getHeldItemOffhand());
		return main.get(getCurrentItem()).setStack(entity.inventory.getStackInSlot(getCurrentItem()));
	}

	public ItemStack getStackInArmourSlot(int slotId) {
		if (slotId >= armor.size())
			return ItemStack.EMPTY;
		return armor.get(slotId).setStack(entity.inventory.armorInventory.get(slotId));
	}

	public boolean hasElytra() {
		net.minecraft.item.ItemStack chest = net.minecraft.client.Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		return chest.getItem() == Items.ELYTRA;
	}

}
