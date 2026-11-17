package me.deftware.client.framework.entity.types.main;

import net.minecraft.world.inventory.ContainerInput;

/**
 * @author Deftware
 */
public enum WindowClickAction {

	THROW(ContainerInput.THROW), QUICK_MOVE(ContainerInput.QUICK_MOVE), PICKUP(ContainerInput.PICKUP);

	private final ContainerInput actionType;

	WindowClickAction(ContainerInput actionType) {
		this.actionType = actionType;
	}

	public ContainerInput getMinecraftActionType() {
		return actionType;
	}

}
