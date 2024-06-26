package me.deftware.client.framework.minecraft;

import me.deftware.client.framework.message.GameChat;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.main.MainEntityPlayer;
import me.deftware.client.framework.gui.screens.GenericScreen;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.render.WorldEntityRenderer;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.client.framework.session.AccountSession;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.WorldTimer;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;

import net.minecraft.client.session.Session;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * @since 17.0.0
 * @author Deftware
 */
public interface Minecraft {

	static Minecraft getMinecraftGame() {
		return (Minecraft) MinecraftClient.getInstance();
	}

	/**
	 * @return The in-game player entity
	 */
	@Nullable
	default MainEntityPlayer _getPlayer() {
		if (this.getClientWorld() == null)
			return null;
		return this.getClientWorld().getEntityByReference(MinecraftClient.getInstance().player);
	}

	/**
	 * @return The in-game camera entity
	 */
	@Nullable
	default Entity _getCameraEntity() {
		if (this.getClientWorld() == null)
			return null;
		return this.getClientWorld().getEntityByReference(MinecraftClient.getInstance().cameraEntity);
	}

	/**
	 * @return The in-game chat
	 */
	default GameChat getGameChat() {
		return (GameChat) MinecraftClient.getInstance().inGameHud.getChatHud();
	}

	WorldEntityRenderer getWorldEntityRenderer();

	GameCamera getCamera();

	/**
	 * @return The current game directory
	 */
	default File _getGameDir() {
		return MinecraftClient.getInstance().runDirectory;
	}

	/**
	 * @return The client world
	 */
	@Nullable
	ClientWorld getClientWorld();

	/**
	 * @return The world timer
	 */
	WorldTimer getWorldTimer();

	/**
	 * @return The current connected server
	 */
	@Nullable
	ServerDetails getConnectedServer();

	/**
	 * @return The last connected server
	 */
	@Nullable
	ServerDetails getLastConnectedServer();

	/**
	 * Sets the game screen
	 */
	void openScreen(GenericScreen screen);

	/**
	 * @return The player chat sender
	 */
	Chat getChatSender();

	/**
	 * @return The current screen
	 */
	@Nullable
	MinecraftScreen getScreen();

	/**
	 * @return If the game is connected to realms server
	 */
	boolean _isOnRealms();

	/**
	 * @return If the game is in a single player server
	 */
	boolean _isSinglePlayer();

	/**
	 * @return The name of the current singleplayer world
	 */
	String _getWorldName();

	/**
	 * @return If the player is looking at a block or an entity
	 */
	boolean isMouseOver();

	/**
	 * @return The current block the player is looking at
	 */
	@Nullable
	BlockSwingResult getHitBlock();

	/**
	 * @return The current entity the player is looking at
	 */
	@Nullable
	Entity getHitEntity();

	/**
	 * Executes a task on the render thread
	 *
	 * @param runnable An action
	 */
	void runOnRenderThread(Runnable runnable);

	/**
	 * Takes a screenshot
	 *
	 * @param file Save location
	 */
	void screenshot(File file) throws IOException;

	/**
	 * @return The current Minecraft version
	 */
	static String getMinecraftVersion() {
		return SharedConstants.getGameVersion().getName();
	}

	/**
	 * @return The current protocol version
	 */
	static int getMinecraftProtocolVersion() {
		return SharedConstants.getGameVersion().getProtocolVersion();
	}

	/**
	 * @return A list of debug hud info modifiers
	 */
	List<Function<List<String>, List<String>>> getDebugModifiers();

	/**
	 * @return The game FPS
	 */
	int getFPS();

	/**
	 * Exits the game
	 */
	void shutdown();

	/**
	 * @return The current session
	 */
	@ApiStatus.Internal
	Session getSession();

	/**
	 * Sets the current session
	 */
	@ApiStatus.Internal
	void setSession(AccountSession session);

	void doRightClickMouse();

	void doClickMouse();

	void doMiddleClickMouse();

	void setRightClickDelayTimer(int delay);

	void setLastConnected(ServerDetails details);

}
