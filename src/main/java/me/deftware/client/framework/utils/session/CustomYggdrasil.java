package me.deftware.client.framework.utils.session;

import java.util.StringJoiner;

/**
 * Custom implementation of the YggdrasilEnvironment allowing the use of custom auth hosts
 */
public class CustomYggdrasil {

	String authHost, accountsHost, sessionHost;

	/**
	 * The provided URLs should NOT include a trailing slash.
	 */
	public CustomYggdrasil(String authHost, String accountsHost, String sessionHost) {
		this.authHost = authHost;
		this.accountsHost = accountsHost;
		this.sessionHost = sessionHost;
	}

	public String getAuthHost() {
		return authHost;
	}

	public String getAccountsHost() {
		return accountsHost;
	}

	public String getSessionHost() {
		return sessionHost;
	}

	public String getName() {
		return "PROD";
	}

	public String asString() {
		return new StringJoiner(", ", "", "")
				.add("authHost='" + authHost + "'")
				.add("accountsHost='" + accountsHost + "'")
				.add("sessionHost='" + sessionHost + "'")
				.add("name='" + getName() + "'")
				.toString();
	}

}
