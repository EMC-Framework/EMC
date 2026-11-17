package me.deftware.client.framework.minecraft;

import net.minecraft.client.CameraType;

/**
 * @author Deftware
 */
public enum PlayerPerspective {

	FIRST_PERSON(CameraType.FIRST_PERSON),
	THIRD_PERSON_BACK(CameraType.THIRD_PERSON_BACK),
	THIRD_PERSON_FRONT(CameraType.THIRD_PERSON_FRONT);

	private final CameraType perspective;

	PlayerPerspective(CameraType perspective) {
		this.perspective = perspective;
	}

	public CameraType getMinecraftPerspective() {
		return perspective;
	}

	public boolean isThirdPerson() {
		return this == THIRD_PERSON_BACK || this == THIRD_PERSON_FRONT;
	}

}
