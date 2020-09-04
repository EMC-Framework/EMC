package me.deftware.client.framework.helper;

import me.deftware.client.framework.item.ItemStack;
import me.deftware.mixin.imp.IMixinShulkerBoxScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;

import java.util.Objects;

/**
 * @author Deftware
 */
public class ContainerHelper {

	private static Container getCurrent() {
		Container handler = Objects.requireNonNull(MinecraftClient.getInstance().player).container;
		if (handler != null && (handler instanceof GenericContainer || handler instanceof ShulkerBoxContainer)) {
			return MinecraftClient.getInstance().player.container;
		}
		return null;
	}

	public static boolean isOpen() {
		return getCurrent() != null;
	}

	public static Inventory getInventory() {
		Container screenHandler = Objects.requireNonNull(getCurrent());
		if (screenHandler instanceof ShulkerBoxContainer) {
			return ((IMixinShulkerBoxScreenHandler) screenHandler).getInventory();
		}
		return ((GenericContainer) screenHandler).getInventory();
	}

	public static boolean isDouble() {
		return getInventory() instanceof DoubleInventory;
	}

	public static int getInventorySize() {
		return getInventory().getInvSize();
	}

	public static ItemStack getStackInSlot(int id) {
		return new ItemStack(getInventory().getInvStack(id));
	}

	public static int getContainerID() {
		return Objects.requireNonNull(getCurrent()).syncId;
	}

	public static boolean isEmpty() {
		return getInventory().isInvEmpty();
	}

	public static int getMaxSlots() {
		return Objects.requireNonNull(getCurrent()).slotList.size();
	}

}
