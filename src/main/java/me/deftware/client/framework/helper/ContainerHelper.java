package me.deftware.client.framework.helper;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.mixin.imp.IMixinShulkerBoxScreenHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.*;

import java.util.Objects;

/**
 * @author Deftware
 */
public class ContainerHelper {

	private static Container getCurrent() {
		Container handler = Objects.requireNonNull(Minecraft.getInstance().player).openContainer;
		if (handler != null && (handler instanceof ContainerChest || handler instanceof ContainerShulkerBox)) {
			return handler;
		}
		return null;
	}

	public static boolean isOpen() {
		return getCurrent() != null;
	}

	public static IInventory getInventory() {
		Container screenHandler = Objects.requireNonNull(getCurrent());
		if (screenHandler instanceof ContainerShulkerBox) {
			return ((IMixinShulkerBoxScreenHandler) screenHandler).getInventory();
		}
		return ((ContainerChest) screenHandler).getLowerChestInventory();
	}

	public static boolean isDouble() {
		return getInventory() instanceof InventoryLargeChest;
	}

	public static int getInventorySize() {
		return getInventory().getSizeInventory();
	}

	public static ItemStack getStackInSlot(int id) {
		return new ItemStack(getInventory().getStackInSlot(id));
	}

	public static int getContainerID() {
		return Objects.requireNonNull(getCurrent()).windowId;
	}

	public static boolean isEmpty() {
		return getInventory().isEmpty();
	}

	public static int getMaxSlots() {
		return Objects.requireNonNull(getCurrent()).inventorySlots.size();
	}

}
