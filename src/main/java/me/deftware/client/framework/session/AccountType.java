package me.deftware.client.framework.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Deftware
 */
@AllArgsConstructor
public enum AccountType {

	/**
	 * Legacy account with username and password
	 */
	Legacy("legacy"),

	/**
	 * Mojang account email and password account
	 */
	Mojang("mojang"),

	/**
	 * Microsoft account login
	 */
	Microsoft("mojang");

	@Getter
	private final String type;

}
