package me.deftware.client.framework.helper;

/**
 * @author Deftware
 */
public class SessionHelper {

	public static String getSessionId() {
		return net.minecraft.client.Minecraft.getInstance().getSession().getSessionID();
	}

	public static String getPlayerUUID() {
		return net.minecraft.client.Minecraft.getInstance().getSession().getPlayerID();
	}

	public static String getPlayerUsername() {
		return net.minecraft.client.Minecraft.getInstance().getSession().getUsername();
	}

	public static String getAccessToken() {
		return net.minecraft.client.Minecraft.getInstance().getSession().getToken();
	}

}
