package me.deftware.client.framework.gui.screens;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.inventory.Inventory;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.mixin.imp.IMixinShulkerBoxScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.Slot;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Deftware
 */
public interface ContainerScreen extends MinecraftScreen {

	Slot getMinecraftSlot();

	Container getScreenHandler();

	Inventory getContainerInventory();

	Message getInventoryName();

	/*
		Hovered
	 */

	default int getSlotId() {
		return getMinecraftSlot().id;
	}

	default boolean isHovered() {
		return getMinecraftSlot() != null;
	}

	default int getHoveredIndex() {
		return 0;
	}

	default ItemStack getHoveredItemStack() {
		return (ItemStack) getMinecraftSlot().getStack();
	}

	/*
		Container
	 */

	default boolean isPlayerInventory() {
		return this instanceof AbstractInventoryScreen;
	}

	default int getContainerID() {
		return getScreenHandler().syncId;
	}

	default int getMaxSlots() {
		return getScreenHandler().slots.size();
	}

	@ApiStatus.Internal
	default net.minecraft.inventory.Inventory getHandlerInventory() {
		if (getScreenHandler() instanceof IMixinShulkerBoxScreenHandler) {
			return ((IMixinShulkerBoxScreenHandler) getScreenHandler()).getInventory();
		} else if (getScreenHandler() instanceof GenericContainer) {
			return ((GenericContainer) getScreenHandler()).getInventory();
		}
		return null;
	}

	@Override
	default void close() {
		if (getScreenHandler() != null) {
			MinecraftClient.getInstance().player.closeContainer();
			return;
		}
		MinecraftScreen.super.close();
	}

}
