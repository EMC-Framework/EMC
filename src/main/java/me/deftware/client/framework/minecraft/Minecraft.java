package me.deftware.client.framework.minecraft;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.main.MainEntityPlayer;
import me.deftware.client.framework.gui.screens.GenericScreen;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.render.WorldEntityRenderer;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.Session;

import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.WorldTimer;

import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus;
import java.io.File;
import java.util.List;
import java.util.function.Function;

/**
 * @since 17.0.0
 * @author Deftware
 */
public interface Minecraft {

	static Minecraft getMinecraftGame() {
		return (Minecraft) net.minecraft.client.Minecraft.getMinecraft();
	}

	/**
	 * @return The in-game player entity
	 */
	@Nullable
	default MainEntityPlayer _getPlayer() {
		if (this.getClientWorld() == null)
			return null;
		return this.getClientWorld().getEntityByReference(net.minecraft.client.Minecraft.getMinecraft().thePlayer);
	}

	/**
	 * @return The in-game camera entity
	 */
	@Nullable
	default Entity _getCameraEntity() {
		if (this.getClientWorld() == null)
			return null;
		return this.getClientWorld().getEntityByReference(net.minecraft.client.Minecraft.getMinecraft().getRenderViewEntity());
	}

	WorldEntityRenderer getWorldEntityRenderer();

	GameCamera getCamera();

	/**
	 * @return The current game directory
	 */
	default File _getGameDir() {
		return net.minecraft.client.Minecraft.getMinecraft().mcDataDir;
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
	 * @return Client options
	 */
	ClientOptions getClientOptions();

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
	 * @return The current Minecraft version
	 */
	static String getMinecraftVersion() {
		return RealmsSharedConstants.VERSION_STRING;
	}

	/**
	 * @return The current protocol version
	 */
	static int getMinecraftProtocolVersion() {
		return RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;
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
	void setSession(Session session);

	/**
	 * Sets the current session service
	 */
	@ApiStatus.Internal
	void setSessionService(MinecraftSessionService service);

	void doRightClickMouse();

	void doClickMouse();

	void doMiddleClickMouse();

	void setRightClickDelayTimer(int delay);

}
