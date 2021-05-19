package me.deftware.client.framework.minecraft;

import me.deftware.client.framework.conversion.ComparedConversion;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.types.main.MainEntityPlayer;
import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.helper.ScreenHelper;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import me.deftware.client.framework.util.minecraft.ServerConnectionInfo;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.math.RayTraceResult;

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

	private static final ComparedConversion<EntityPlayerSP, MainEntityPlayer> mainPlayer =
			new ComparedConversion<>(() -> net.minecraft.client.Minecraft.getMinecraft().player, MainEntityPlayer::new);

	private static final ComparedConversion<net.minecraft.entity.Entity, Entity> cameraEntity =
			new ComparedConversion<>(() -> net.minecraft.client.Minecraft.getMinecraft().getRenderViewEntity(), Entity::newInstance);

	private static final ComparedConversion<net.minecraft.entity.Entity, Entity> hitEntity =
			new ComparedConversion<>(() -> {
				if (net.minecraft.client.Minecraft.getMinecraft().objectMouseOver != null) {
					if (net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
						return net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.entityHit;
					}
				}
				return null;
			}, Entity::newInstance);

	private static final ComparedConversion<RayTraceResult, BlockSwingResult> hitBlock =
			new ComparedConversion<>(() -> {
				if (net.minecraft.client.Minecraft.getMinecraft().objectMouseOver != null) {
					if (net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
						return net.minecraft.client.Minecraft.getMinecraft().objectMouseOver;
					}
				}
				return null;
			}, BlockSwingResult::new);

	private static final ComparedConversion<ServerData, ServerConnectionInfo> connectedServer =
			new ComparedConversion<>(() -> net.minecraft.client.Minecraft.getMinecraft().getCurrentServerData(), ServerConnectionInfo::new);

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
			int chatHeight = 24, multiplier = getRealScaledMultiplier(), scaleFactor = (int) net.minecraft.client.Minecraft.getMinecraft().gameSettings.guiScale;
			if (scaleFactor == 0) return chatHeight;
			chatHeight *= multiplier;
			if (scaleFactor % 2 == 0) chatHeight += 5;
			return chatHeight;
		}
		return 0;
	}

	public static int getMinecraftChatScaledXOffset() {
		if (ScreenHelper.isChatOpen()) {
			int chatX = 4, multiplier = getRealScaledMultiplier(), scaleFactor = (int) net.minecraft.client.Minecraft.getMinecraft().gameSettings.guiScale;
			if (scaleFactor == 0) return chatX;
			chatX *= multiplier;
			return chatX;
		}
		return 4;
	}

	public static int getRealScaledMultiplier() {
		int scaleFactor = (int) net.minecraft.client.Minecraft.getMinecraft().gameSettings.guiScale;
		if (scaleFactor != 0) {
			scaleFactor /= scaleFactor % 2 == 0 ? 2 : 1.8;
		}
		return scaleFactor;
	}

	public static void openScreen(GuiScreen screen) {
		net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(screen);
	}

	public static String getRunningLocation() throws URISyntaxException {
		return new File(Minecraft.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
	}

	public static void connectToServer(ServerConnectionInfo server) {
		net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new GuiMultiplayer(null), net.minecraft.client.Minecraft.getMinecraft(), server));
	}

	public static void openChat(String originText) {
		if (net.minecraft.client.Minecraft.getMinecraft().currentScreen == null) {
			net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new GuiChat(originText));
		}
	}

	public static boolean isSingleplayer() {
		return net.minecraft.client.Minecraft.getMinecraft().isSingleplayer();
	}

	public static boolean isInGame() {
		return net.minecraft.client.Minecraft.getMinecraft().currentScreen == null;
	}

	public static File getRunDir() {
		return net.minecraft.client.Minecraft.getMinecraft().gameDir;
	}

	public static GameCamera getCamera() {
		return camera;
	}

	@Nullable
	public static GuiScreen getScreen() {
		if (net.minecraft.client.Minecraft.getMinecraft().currentScreen != null) {
			if (net.minecraft.client.Minecraft.getMinecraft().currentScreen instanceof GuiScreen) {
				return (GuiScreen) net.minecraft.client.Minecraft.getMinecraft().currentScreen;
			}
		}
		return null;
	}

	public static int getViewDistance() {
		return net.minecraft.client.Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
	}

	public static PlayerPerspective getPerspective() {
		return net.minecraft.client.Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 ?
				PlayerPerspective.FIRST_PERSON : net.minecraft.client.Minecraft.getMinecraft().gameSettings.thirdPersonView == 1 ?
				PlayerPerspective.THIRD_PERSON_BACK : PlayerPerspective.THIRD_PERSON_FRONT;
	}

	public static void shutdown() {
		net.minecraft.client.Minecraft.getMinecraft().shutdown();
	}

	public static String getMinecraftVersion() {
		return RealmsSharedConstants.VERSION_STRING;
	}

	public static int getMinecraftProtocolVersion() {
		return RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;
	}

	public static boolean isMouseOver() {
		return net.minecraft.client.Minecraft.getMinecraft().objectMouseOver != null;
	}

	@Nullable
	public static ServerConnectionInfo getConnectedServer() {
		return connectedServer.get();
	}
	
	public static boolean isOnRealms() {
		return net.minecraft.client.Minecraft.getMinecraft().isConnectedToRealms();
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
		return net.minecraft.client.Minecraft.getMinecraft().getRenderPartialTicks();
	}

}
