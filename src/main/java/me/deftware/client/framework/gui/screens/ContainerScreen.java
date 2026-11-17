package me.deftware.client.framework.gui.screens;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.inventory.Inventory;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.mixin.imp.IMixinShulkerBoxScreenHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Deftware
 */
public interface ContainerScreen extends MinecraftScreen {

	Slot getMinecraftSlot();

	AbstractContainerMenu getScreenHandler();

	Inventory getContainerInventory();

	Message getInventoryName();

	/*
		Hovered
	 */

	default int getSlotId() {
		return getMinecraftSlot().index;
	}

	default boolean isHovered() {
		return getMinecraftSlot() != null;
	}

	default int getHoveredIndex() {
		return getMinecraftSlot().getContainerSlot();
	}

	default ItemStack getHoveredItemStack() {
		return (ItemStack) getMinecraftSlot().getItem();
	}

	/*
		Container
	 */

	default boolean isPlayerInventory() {
		return this instanceof InventoryScreen || this instanceof CreativeModeInventoryScreen;
	}

	default int getContainerID() {
		return getScreenHandler().containerId;
	}

	default int getMaxSlots() {
		return getScreenHandler().slots.size();
	}

	@ApiStatus.Internal
	default net.minecraft.world.Container getHandlerInventory() {
		if (getScreenHandler() instanceof IMixinShulkerBoxScreenHandler screenHandler) {
			return screenHandler.getInventory();
		} else if (getScreenHandler() instanceof ChestMenu screenHandler) {
			return screenHandler.getContainer();
		}
		return null;
	}

	@Override
	default void close() {
		if (getScreenHandler() != null) {
			Minecraft.getInstance().player.closeContainer();
			return;
		}
		MinecraftScreen.super.close();
	}

}
