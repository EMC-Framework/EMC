package me.deftware.client.framework.minecraft;

/**
 * @author Deftware
 */
public enum PlayerPerspective {

	FIRST_PERSON(0),
	THIRD_PERSON_BACK(1),
	THIRD_PERSON_FRONT(2);

	private final int perspective;

	PlayerPerspective(int perspective) {
		this.perspective = perspective;
	}

	public int getMinecraftPerspective() {
		return perspective;
	}

	public boolean isThirdPerson() {
		return this == THIRD_PERSON_BACK || this == THIRD_PERSON_FRONT;
	}

}
