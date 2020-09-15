package me.deftware.client.framework.entity.types.main;

/**
 * @author Deftware
 */
public enum WindowClickAction {

	THROW(4), QUICK_MOVE(1), PICKUP(0);

	private final int actionType;

	WindowClickAction(int actionType) {
		this.actionType = actionType;
	}

	public int getMinecraftActionType() {
		return actionType;
	}

}
