package me.deftware.client.framework.minecraft;

import me.deftware.client.framework.conversion.ComparedConversion;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.main.MainEntityPlayer;
import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.helper.ScreenHelper;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import me.deftware.client.framework.util.minecraft.ServerConnectionInfo;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ingame.ChatScreen;
import net.minecraft.client.gui.menu.MultiplayerScreen;
import net.minecraft.client.gui.menu.ServerConnectingScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.ServerEntry;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Deftware
 */
public class Minecraft {

	public static final Queue<Runnable> RENDER_THREAD = new ConcurrentLinkedQueue<>();

	private static final ComparedConversion<ClientPlayerEntity, MainEntityPlayer> mainPlayer =
			new ComparedConversion<>(() -> net.minecraft.client.Minecraft.getInstance().player, MainEntityPlayer::new);

	private static final ComparedConversion<net.minecraft.entity.Entity, Entity> cameraEntity =
			new ComparedConversion<>(() -> net.minecraft.client.Minecraft.getInstance().cameraEntity, Entity::newInstance);

	private static final ComparedConversion<net.minecraft.entity.Entity, Entity> hitEntity =
			new ComparedConversion<>(() -> {
				if (net.minecraft.client.Minecraft.getInstance().hitResult != null) {
					if (net.minecraft.client.Minecraft.getInstance().hitResult.getType() == HitResult.Type.ENTITY) {
						return ((EntityHitResult) net.minecraft.client.Minecraft.getInstance().hitResult).getEntity();
					}
				}
				return null;
			}, Entity::newInstance);

	private static final ComparedConversion<HitResult, BlockSwingResult> hitBlock =
			new ComparedConversion<>(() -> {
				if (net.minecraft.client.Minecraft.getInstance().hitResult != null) {
					if (net.minecraft.client.Minecraft.getInstance().hitResult.getType() == HitResult.Type.BLOCK) {
						return net.minecraft.client.Minecraft.getInstance().hitResult;
					}
				}
				return null;
			}, BlockSwingResult::new);

	private static final ComparedConversion<ServerEntry, ServerConnectionInfo> connectedServer =
			new ComparedConversion<>(() -> net.minecraft.client.Minecraft.getInstance().getCurrentServerEntry(), ServerConnectionInfo::new);

	public static ServerConnectionInfo lastConnectedServer = null;

	private static final GameCamera camera = new GameCamera();

	/**
	 * The main player
	 */
	@Nullable
	public static MainEntityPlayer getPlayer() {
		return mainPlayer.get();
	}

	@Nullable
	public static Entity getCameraEntity() {
		return cameraEntity.get();
	}

	public static int getMinecraftChatScaledYOffset() {
		if (ScreenHelper.isChatOpen()) {
			int chatHeight = 24, multiplier = getRealScaledMultiplier(), scaleFactor = (int) net.minecraft.client.Minecraft.getInstance().window.getScaleFactor();
			if (scaleFactor == 0) return chatHeight;
			chatHeight *= multiplier;
			if (scaleFactor % 2 == 0) chatHeight += 5;
			return chatHeight;
		}
		return 0;
	}

	public static int getMinecraftChatScaledXOffset() {
		if (ScreenHelper.isChatOpen()) {
			int chatX = 4, multiplier = getRealScaledMultiplier(), scaleFactor = (int) net.minecraft.client.Minecraft.getInstance().window.getScaleFactor();
			if (scaleFactor == 0) return chatX;
			chatX *= multiplier;
			return chatX;
		}
		return 4;
	}

	public static int getRealScaledMultiplier() {
		int scaleFactor = (int) net.minecraft.client.Minecraft.getInstance().window.getScaleFactor();
		if (scaleFactor != 0) {
			scaleFactor /= scaleFactor % 2 == 0 ? 2 : 1.8;
		}
		return scaleFactor;
	}

	public static void openScreen(GuiScreen screen) {
		net.minecraft.client.Minecraft.getInstance().displayGuiScreen(screen);
	}

	public static String getRunningLocation() throws URISyntaxException {
		return new File(Minecraft.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
	}

	public static void connectToServer(ServerConnectionInfo server) {
		net.minecraft.client.Minecraft.getInstance().openScreen(new ServerConnectingScreen(new MultiplayerScreen(null), net.minecraft.client.Minecraft.getInstance(), server));
	}

	public static void openChat(String originText) {
		if (net.minecraft.client.Minecraft.getInstance().currentScreen == null && net.minecraft.client.Minecraft.getInstance().overlay == null) {
			net.minecraft.client.Minecraft.getInstance().openScreen(new ChatScreen(originText));
		}
	}

	public static boolean isSingleplayer() {
		return net.minecraft.client.Minecraft.getInstance().isInSingleplayer();
	}

	public static boolean isInGame() {
		return net.minecraft.client.Minecraft.getInstance().currentScreen == null;
	}

	public static File getRunDir() {
		return net.minecraft.client.Minecraft.getInstance().runDirectory;
	}

	public static GameCamera getCamera() {
		return camera;
	}

	@Nullable
	public static GuiScreen getScreen() {
		if (net.minecraft.client.Minecraft.getInstance().currentScreen != null) {
			if (net.minecraft.client.Minecraft.getInstance().currentScreen instanceof GuiScreen) {
				return (GuiScreen) net.minecraft.client.Minecraft.getInstance().currentScreen;
			}
		}
		return null;
	}

	public static PlayerPerspective getPerspective() {
		return net.minecraft.client.Minecraft.getInstance().options.perspective == 0 ?
				PlayerPerspective.FIRST_PERSON : net.minecraft.client.Minecraft.getInstance().options.perspective == 1 ?
				PlayerPerspective.THIRD_PERSON_BACK : PlayerPerspective.THIRD_PERSON_FRONT;
	}

	public static void shutdown() {
		net.minecraft.client.Minecraft.getInstance().stop();
	}

	public static String getMinecraftVersion() {
		return SharedConstants.getGameVersion().getName();
	}

	public static int getMinecraftProtocolVersion() {
		return SharedConstants.getGameVersion().getProtocolVersion();
	}

	public static boolean isMouseOver() {
		return net.minecraft.client.Minecraft.getInstance().hitResult != null;
	}

	@Nullable
	public static ServerConnectionInfo getConnectedServer() {
		return connectedServer.get();
	}

	@Nullable
	public static ServerConnectionInfo getLastConnectedServer() {
		return lastConnectedServer;
	}

	@Nullable
	public static Entity getHitEntity() {
		return hitEntity.get();
	}

	@Nullable
	public static BlockSwingResult getHitBlock() {
		return hitBlock.get();
	}

	public static float getRenderPartialTicks() {
		return net.minecraft.client.Minecraft.getInstance().getTickDelta();
	}

}
