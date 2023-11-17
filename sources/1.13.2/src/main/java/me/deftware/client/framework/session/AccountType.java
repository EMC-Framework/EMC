package me.deftware.client.framework.session;

/**
 * @author Deftware
 */
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

	private final String type;

	AccountType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
