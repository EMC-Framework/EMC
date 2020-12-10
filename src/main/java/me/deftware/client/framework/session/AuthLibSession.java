package me.deftware.client.framework.session;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.deftware.mixin.imp.IMixinMinecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Allows modifying and creating sessions
 *
 * @author Deftware
 */
public class AuthLibSession {

	private Session session;
	private final UserAuthentication userAuthentication;
	private final YggdrasilAuthenticationService authenticationService;

	public AuthLibSession(CustomYggdrasil yggdrasil) {
		authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
		userAuthentication = new YggdrasilUserAuthentication(authenticationService, Agent.MINECRAFT);
	}

	public AuthLibSession() {
		this(null);
	}

	public void setCredentials(String username, String password) {
		userAuthentication.setUsername(username);
		userAuthentication.setPassword(password);
	}

	public void logout() {
		userAuthentication.logOut();
	}

	public CompletableFuture<Boolean> login() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				userAuthentication.logIn();
				return true;
			} catch (AuthenticationException ignored) { }
			return false;
		});
	}

	public boolean loggedIn() {
		return userAuthentication != null && session != null && userAuthentication.isLoggedIn();
	}

	public Session buildSession() {
		if (!userAuthentication.isLoggedIn()) throw new RuntimeException("Cannot create session without logging in!");
		if (session != null) {
			return session;
		}
		session = new Session(userAuthentication.getSelectedProfile().getName(), userAuthentication.getSelectedProfile().getId().toString(),
				userAuthentication.getAuthenticatedToken(), userAuthentication.getSelectedProfile().getName().contains("@") ? "mojang" : "legacy");
		return session;
	}

	public void setSession() {
		setSession(buildSession());
	}

	public void setSession(Session session) {
		((IMixinMinecraft) MinecraftClient.getInstance()).setSession(buildSession());
		((IMixinMinecraft) MinecraftClient.getInstance()).setSessionService(new CustomSessionService(authenticationService));
	}

	public void setOfflineSession(String username) {
		((IMixinMinecraft) MinecraftClient.getInstance()).setSession(new Session(username, "", "0", "legacy"));
	}

	public void setManualSession(String username, String uuid, String accessToken) {
		Session session = new Session(username, uuid, accessToken, "mojang");
		Map<String, Object> map = new HashMap<>();
		map.put("accessToken", accessToken);
		map.put("displayName", username);
		map.put("uuid", uuid);
		userAuthentication.loadFromStorage(map);
		((IMixinMinecraft) MinecraftClient.getInstance()).setSession(session);
		((IMixinMinecraft) MinecraftClient.getInstance()).setSessionService(new CustomSessionService(authenticationService));
	}

}
