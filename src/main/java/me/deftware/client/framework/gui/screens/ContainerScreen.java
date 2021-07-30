package me.deftware.client.framework.gui.screens;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.inventory.Inventory;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.mixin.imp.IMixinShulkerBoxScreenHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Deftware
 */
public interface ContainerScreen extends MinecraftScreen {

	Slot getMinecraftSlot();

	Container getScreenHandler();

	Inventory getContainerInventory();

	ChatMessage getInventoryName();

	/*
		Hovered
	 */

	default int getSlotId() {
		return getMinecraftSlot().slotNumber;
	}

	default boolean isHovered() {
		return getMinecraftSlot() != null;
	}

	default int getHoveredIndex() {
		return 0;
	}

	default ItemStack getHoveredItemStack() {
		return new ItemStack(getMinecraftSlot().getStack());
	}

	/*
		Container
	 */

	default boolean isPlayerInventory() {
		return this instanceof InventoryEffectRenderer;
	}

	default int getContainerID() {
		return getScreenHandler().windowId;
	}

	default int getMaxSlots() {
		return getScreenHandler().inventorySlots.size();
	}

	@ApiStatus.Internal
	default IInventory getHandlerInventory() {
		if (getScreenHandler() instanceof IMixinShulkerBoxScreenHandler) {
			return ((IMixinShulkerBoxScreenHandler) getScreenHandler()).getInventory();
		} else if (getScreenHandler() instanceof ContainerChest) {
			return ((ContainerChest) getScreenHandler()).getLowerChestInventory();
		}
		return null;
	}

	@Override
	default void close() {
		if (getScreenHandler() != null) {
			Minecraft.getMinecraft().thePlayer.closeScreen();
			return;
		}
		MinecraftScreen.super.close();
	}

}
