package me.deftware.client.framework.session;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;
import java.util.Map;
import java.util.UUID;

/**
 * @author Deftware
 * @since 17.0.0
 */
public class AccountSession {

	/**
	 * The account type
	 */
	private AccountType type = AccountType.Mojang;

	/**
	 * Unique client ID which needs to be used for every request
	 */
	private final String clientId = UUID.randomUUID().toString();

	/**
	 * Minecraft session object
	 */
	private Session session;

	private final UserAuthentication userAuthentication;
	private final YggdrasilAuthenticationService authenticationService;

	public AccountSession(AuthEnvironment environment) {
		// Set up authentication service
		this.authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, clientId);
		this.userAuthentication = new YggdrasilUserAuthentication(authenticationService, Agent.MINECRAFT);
	}

	/**
	 * Logs into a Mojang or legacy account
	 *
	 * @param username Account username
	 * @param password Account password
	 * @throws Exception Authentication exception
	 */
	public AccountSession withCredentials(String username, String password) throws Exception {
		this.type = username.contains("@") ? AccountType.Mojang : AccountType.Legacy;
		this.userAuthentication.setUsername(username);
		this.userAuthentication.setPassword(password);
		this.userAuthentication.logIn();
		// Set session
		this.session = new Session(userAuthentication.getSelectedProfile().getName(), userAuthentication.getSelectedProfile().getId().toString(),
				userAuthentication.getAuthenticatedToken(), this.type.toString());
		return this;
	}

	/**
	 * Creates an offline session
	 *
	 * @param username Any username
	 */
	public AccountSession withOfflineUsername(String username) {
		this.type = AccountType.Legacy;
		this.session = new Session(username, "", "0", this.type.getType());
		return this;
	}

	/**
	 * Logs into a custom provided session
	 *
	 * @param map Account session details
	 * @param accountType Account type
	 */
	public AccountSession withSession(Map<String, Object> map, AccountType accountType) {
		this.type = accountType;
		this.session = new Session(map.get("username").toString(), map.get("uuid").toString(), map.get("accessToken").toString(), this.type.getType());
		this.userAuthentication.loadFromStorage(map);
		return this;
	}

	/**
	 * @return If the session has been created
	 */
	public boolean isSessionAvailable() {
		return this.session != null;
	}

	/**
	 * @return The session username
	 */
	public String getSessionUsername() {
		return session.getProfile().getName();
	}

	/**
	 * @return The session uuid
	 */
	public UUID getSessionUUID() {
		return session.getProfile().getId();
	}

	/**
	 * Sets the Minecraft session to this session
	 */
	public AccountSession setSession() {
		Minecraft minecraft = Minecraft.getMinecraftGame();
		minecraft.setSession(this.session);
		minecraft.setSessionService(this.authenticationService.createMinecraftSessionService());
		return this;
	}

}
