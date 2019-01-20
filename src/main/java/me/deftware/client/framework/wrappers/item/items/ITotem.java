package me.deftware.client.framework.wrappers.item.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ITotem {

	public static int getSlot() {
		InventoryPlayer in = Minecraft.getInstance().player.inventory;
		for (int i = 0; i < in.mainInventory.size() + 1; i++) {
			try {
				ItemStack it = in.mainInventory.get(i);
				if (it.getItem().getTranslationKey().equals("item.minecraft.totem_of_undying")) {
					return i;
				}
			} catch (Exception ex) {
			}
		}
		return -1;
	}

	public static boolean hasTotem() {
		InventoryPlayer in = Minecraft.getInstance().player.inventory;
		for (int i = 0; i < in.mainInventory.size() + 1; i++) {
			try {
				ItemStack it = in.mainInventory.get(i);
				if (it.getItem().getTranslationKey().equals("item.minecraft.totem_of_undying")) {
					return true;
				}
			} catch (Exception ex) {
			}
		}
		return false;
	}

	public static void swapItems(int one, int two, int windowId) {
		Minecraft.getInstance().playerController.windowClick(windowId, one, 0, ClickType.SWAP,
				Minecraft.getInstance().player);
		Minecraft.getInstance().playerController.windowClick(windowId, two, 0, ClickType.SWAP,
				Minecraft.getInstance().player);
		Minecraft.getInstance().playerController.tick();
	}

	public static void moveItem() {
		Minecraft.getInstance().playerController.windowClick(0, ITotem.getSlot(), 0, ClickType.QUICK_MOVE,
				Minecraft.getInstance().player);
	}

}
