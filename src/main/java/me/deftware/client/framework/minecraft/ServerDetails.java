package me.deftware.client.framework.minecraft;

import me.deftware.client.framework.message.Message;

/**
 * @since 17.0.0
 * @author Deftware
 */
public interface ServerDetails {

	/**
	 * @return The server name
	 */
	String _getName();

	/**
	 * @return The server address
	 */
	String _getAddress();

	/**
	 * @return Message of the day
	 */
	Message _getMotd();

	/**
	 * @return The players tooltip
	 */
	Message _getPlayers();

	/**
	 * @return If the server is online
	 */
	boolean _isOnline();

	/**
	 * @return If the server is a lan server
	 */
	boolean _isLan();

}
