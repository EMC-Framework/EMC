package me.deftware.client.framework.render.camera.entity;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.world.World;
import net.minecraft.client.Minecraft;
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
		if (World.isLoaded()) {
			fakePlayer = new CameraEntity(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world),
					Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).getGameProfile(),
					Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).getHungerManager());
			fakePlayer.copyPositionAndRotation(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player));
			fakePlayer.setHeadYaw(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).headYaw);
			fakePlayer.spawn();
			savedPerspective = Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().options).perspective;
			Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().options).perspective = 0;
			net.minecraft.client.Minecraft.getInstance().setCameraEntity(fakePlayer);
			if (net.minecraft.client.Minecraft.getInstance().player.input instanceof KeyboardInput) net.minecraft.client.Minecraft.getInstance().player.input = new DummyInput();
		}
	}

	public static boolean isCameraEntity(Entity entity) {
		return entity.getMinecraftEntity() == fakePlayer;
	}

	public static void disable() {
		if (World.isLoaded()) {
			net.minecraft.client.Minecraft.getInstance().options.perspective = savedPerspective;
			net.minecraft.client.Minecraft.getInstance().setCameraEntity(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player));
			if (fakePlayer != null) fakePlayer.despawn();
			fakePlayer = null;
			if (net.minecraft.client.Minecraft.getInstance().player.input instanceof DummyInput) net.minecraft.client.Minecraft.getInstance().player.input = new KeyboardInput(net.minecraft.client.Minecraft.getInstance().options);
		}
	}

}
