package me.deftware.client.framework.helper;

import net.minecraft.client.Minecraft;

/**
 * @author Deftware
 */
public class SessionHelper {

	public static String getSessionId() {
		return Minecraft.getInstance().getUser().getSessionId();
	}

	public static String getPlayerUUID() {
		var uuid = Minecraft.getInstance().getUser().getProfileId();
		if (uuid != null) {
			return uuid.toString();
		}
		return "0";
	}

	public static String getPlayerUsername() {
		return Minecraft.getInstance().getUser().getName();
	}

	public static String getAccessToken() {
		return Minecraft.getInstance().getUser().getAccessToken();
	}

}
