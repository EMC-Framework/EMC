package me.deftware.client.framework.render.camera.entity;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInputFromOptions;

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
			fakePlayer = new CameraEntity(Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().world),
					Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().player).getGameProfile(),
					Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().player).getFoodStats());
			fakePlayer.copyLocationAndAnglesFrom(Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().player));
			fakePlayer.setRotationYawHead(Objects.requireNonNull(Minecraft.getMinecraft().player).rotationYawHead);
			fakePlayer.spawn();
			savedPerspective = Objects.requireNonNull(Minecraft.getMinecraft().gameSettings).thirdPersonView;
			Objects.requireNonNull(Minecraft.getMinecraft().gameSettings).thirdPersonView = 0;
			net.minecraft.client.Minecraft.getMinecraft().setRenderViewEntity(fakePlayer);
			if (Minecraft.getMinecraft().player.movementInput instanceof MovementInputFromOptions) Minecraft.getMinecraft().player.movementInput = new DummyInput();
		}
	}

	public static boolean isCameraEntity(Entity entity) {
		return entity.getMinecraftEntity() == fakePlayer;
	}

	public static void disable() {
		if (World.isLoaded()) {
			Minecraft.getMinecraft().gameSettings.thirdPersonView = savedPerspective;
			net.minecraft.client.Minecraft.getMinecraft().setRenderViewEntity(Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().player));
			if (fakePlayer != null) fakePlayer.despawn();
			fakePlayer = null;
			if (Minecraft.getMinecraft().player.movementInput instanceof DummyInput) Minecraft.getMinecraft().player.movementInput = new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
		}
	}

}
