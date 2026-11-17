package me.deftware.client.framework.render.camera.entity;

import com.mojang.authlib.GameProfile;
import me.deftware.client.framework.entity.Entity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import java.util.Objects;
import java.util.UUID;

/**
 * Based on https://github.com/wagyourtail/Freecam
 *
 * @author wagyourtail, Deftware
 */
public class CameraEntityMan {

	public static float speed = 0.25f;
	public static CameraEntity fakePlayer;
	private static CameraType savedPerspective;

	public static boolean isActive() {
		return fakePlayer != null;
	}

	public static void enable() {
		var mc = Minecraft.getInstance();
		var player = Objects.requireNonNull(mc.player);
		var profile = new GameProfile(UUID.randomUUID(), player.getGameProfile().name());
		fakePlayer = new CameraEntity(Objects.requireNonNull(mc.level), profile, player.getFoodData());
		fakePlayer.copyPosition(player);
		fakePlayer.setYHeadRot(player.yHeadRot);
		fakePlayer.spawn();
		savedPerspective = Objects.requireNonNull(mc.options).getCameraType();
		mc.options.setCameraType(CameraType.FIRST_PERSON);
		mc.setCameraEntity(fakePlayer);
		if (player.input instanceof KeyboardInput)
			player.input = new DummyInput();
	}

	public static boolean isCameraEntity(Entity entity) {
		return entity.getMinecraftEntity() == fakePlayer;
	}

	public static void disable() {
		Minecraft mc = Minecraft.getInstance();
		if (savedPerspective != null) {
			mc.options.setCameraType(savedPerspective);
			savedPerspective = null;
		}
		if (mc.player != null) {
			mc.setCameraEntity(mc.player);
			if (mc.player.input instanceof DummyInput) {
				mc.player.input = new KeyboardInput(mc.options);
			}
		}
		if (fakePlayer != null) {
			fakePlayer.despawn();
			fakePlayer = null;
		}
	}

}
