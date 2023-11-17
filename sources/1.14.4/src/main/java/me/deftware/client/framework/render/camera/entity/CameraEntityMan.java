package me.deftware.client.framework.render.camera.entity;

import me.deftware.client.framework.entity.Entity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;

import java.util.Objects;

/**
 * Based on https://github.com/wagyourtail/Freecam
 *
 * @author wagyourtail, Deftware
 */
public class CameraEntityMan {

	public static float speed = 0.25f;
	public static CameraEntity fakePlayer;
	private static int savedPerspective;

	public static boolean isActive() {
		return fakePlayer != null;
	}

	public static void enable() {
			fakePlayer = new CameraEntity(Objects.requireNonNull(MinecraftClient.getInstance().world),
					Objects.requireNonNull(MinecraftClient.getInstance().player).getGameProfile(),
					Objects.requireNonNull(MinecraftClient.getInstance().player).getHungerManager());
			fakePlayer.copyPositionAndRotation(Objects.requireNonNull(MinecraftClient.getInstance().player));
			fakePlayer.setHeadYaw(Objects.requireNonNull(MinecraftClient.getInstance().player).headYaw);
			fakePlayer.spawn();
			savedPerspective = Objects.requireNonNull(MinecraftClient.getInstance().options).perspective;
			Objects.requireNonNull(MinecraftClient.getInstance().options).perspective = 0;
			MinecraftClient.getInstance().setCameraEntity(fakePlayer);
			if (MinecraftClient.getInstance().player.input instanceof KeyboardInput) MinecraftClient.getInstance().player.input = new DummyInput();
	}

	public static boolean isCameraEntity(Entity entity) {
		return entity.getMinecraftEntity() == fakePlayer;
	}

	public static void disable() {
		MinecraftClient.getInstance().options.perspective = savedPerspective;
		MinecraftClient.getInstance().setCameraEntity(Objects.requireNonNull(MinecraftClient.getInstance().player));
		if (fakePlayer != null) fakePlayer.despawn();
		fakePlayer = null;
		if (MinecraftClient.getInstance().player.input instanceof DummyInput) MinecraftClient.getInstance().player.input = new KeyboardInput(MinecraftClient.getInstance().options);
	}

}
