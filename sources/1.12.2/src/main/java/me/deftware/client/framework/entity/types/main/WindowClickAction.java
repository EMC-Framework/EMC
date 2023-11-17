package me.deftware.client.framework.entity.types.main;

import net.minecraft.inventory.ClickType;

/**
 * @author Deftware
 */
public enum WindowClickAction {

	THROW(ClickType.THROW), QUICK_MOVE(ClickType.QUICK_MOVE), PICKUP(ClickType.PICKUP);

	private final ClickType actionType;

	WindowClickAction(ClickType actionType) {
		this.actionType = actionType;
	}

	public ClickType getMinecraftActionType() {
		return actionType;
	}

}
