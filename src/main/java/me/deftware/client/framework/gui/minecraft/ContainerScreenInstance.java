package me.deftware.client.framework.gui.minecraft;

import me.deftware.client.framework.inventory.Slot;
import me.deftware.mixin.imp.IMixinGuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author Deftware
 */
public class ContainerScreenInstance extends ScreenInstance {

	private Slot slot;
	private Type type = Type.OTHER;

	public ContainerScreenInstance(GuiScreen screen) {
		super(screen);
		if (((GuiContainer) screen).inventorySlots instanceof ContainerChest) {
			TextComponentTranslation component = (TextComponentTranslation) ((ContainerChest) ((GuiContainer) screen).inventorySlots).getLowerChestInventory().getDisplayName();
			if (component.getKey().equalsIgnoreCase("container.enderchest")) {
				type = Type.ENDER_CHEST;
			}
		}
	}

	public Slot getHoveredSlot() {
		if (((IMixinGuiContainer) getMinecraftScreen()).getHoveredSlot() == null) return null;
		if (slot != null) {
			if (slot.getMinecraftSlot() == ((IMixinGuiContainer) getMinecraftScreen()).getHoveredSlot()) {
				return slot;
			}
		}
		return slot = new Slot(((IMixinGuiContainer) getMinecraftScreen()).getHoveredSlot());
	}

	public Type getContainerType() {
		return type;
	}
	
	public enum Type {
		ENDER_CHEST, OTHER
	}

}
