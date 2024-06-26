package me.deftware.client.framework.helper;

import net.minecraft.client.MinecraftClient;

/**
 * @author Deftware
 */
public class SessionHelper {

	public static String getSessionId() {
		return MinecraftClient.getInstance().getSession().getSessionId();
	}

	public static String getPlayerUUID() {
		var uuid = MinecraftClient.getInstance().getSession().getUuidOrNull();
		if (uuid != null) {
			return uuid.toString();
		}
		return "0";
	}

	public static String getPlayerUsername() {
		return MinecraftClient.getInstance().getSession().getUsername();
	}

	public static String getAccessToken() {
		return MinecraftClient.getInstance().getSession().getAccessToken();
	}

}
