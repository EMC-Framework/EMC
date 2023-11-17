package me.deftware.client.framework.session;

import com.mojang.authlib.Environment;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
import lombok.Getter;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.client.session.Session;
import org.apache.commons.lang3.NotImplementedException;

import java.net.Proxy;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Deftware
 * @since 17.0.0
 */
public class AccountSession {

	/**
	 * The account type
	 */
	private Session.AccountType type = Session.AccountType.MOJANG;

	/**
	 * Authentication environment
	 */
	private final Environment environment;

	/**
	 * Unique client ID which needs to be used for every request
	 */
	private final String clientId = UUID.randomUUID().toString();

	/**
	 * Minecraft session object
	 */
	@Getter
	private Session session;

	private final YggdrasilAuthenticationService authenticationService;

	private AccountSession(Environment environment) {
		this.environment = environment;
		this.authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, environment);
	}

	/**
	 * Creates a new account session with a custom environment
	 *
	 * @param environment Custom Yggdrasil environment
	 */
	public AccountSession(AuthEnvironment environment) {
		this(environment == null ? YggdrasilEnvironment.PROD.getEnvironment() : environment.build());
	}

	/**
	 * Logs into a Mojang or legacy account
	 *
	 * @param username Account username
	 * @param password Account password
	 * @throws Exception Authentication exception
	 */
	public AccountSession withCredentials(String username, String password) throws Exception {
		throw new NotImplementedException("Credentials are no longer supported");
	}

	/**
	 * Creates an offline session
	 *
	 * @param username Any username
	 */
	public AccountSession withOfflineUsername(String username) {
		this.type = Session.AccountType.LEGACY;
		var uuid = UUID.randomUUID();
		this.session = new Session(username, uuid, "0", Optional.empty(), Optional.of(clientId), this.type);
		System.out.println("Assigning UUID " + uuid);
		return this;
	}

	/**
	 * Logs into a custom provided session
	 *
	 * @param map Account session details
	 * @param accountType Account type
	 */
	public AccountSession withSession(Map<String, Object> map, AccountType accountType) {
		this.type = accountType.getType();
		this.session = new Session(map.get("username").toString(), uuidFromString(map.get("uuid").toString()), map.get("accessToken").toString(),
				Optional.of(map.getOrDefault("xuid", "").toString()), Optional.of(clientId), this.type);
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
		return session.getUsername();
	}

	/**
	 * @return The session uuid
	 */
	public UUID getSessionUUID() {
		return session.getUuidOrNull();
	}

	public MinecraftSessionService getSessionService() {
		return authenticationService.createMinecraftSessionService();
	}

	public YggdrasilAuthenticationService getAuthenticationService() {
		return this.authenticationService;
	}

	/**
	 * Sets the Minecraft session to this session
	 */
	@Deprecated
	public AccountSession setSession() {
		Minecraft.getMinecraftGame().setSession(this);
		return this;
	}

	public static UUID uuidFromString(String uuid) {
		if (uuid.contains("-"))
			return UUID.fromString(uuid);
		return UUID.fromString(
				uuid.replaceFirst(
						"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
						"$1-$2-$3-$4-$5"
				)
		);
	}

}
