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
		fakePlayer = new CameraEntity(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world),
				Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).getGameProfile(),
				Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).getFoodStats());
		fakePlayer.copyLocationAndAnglesFrom(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player));
		fakePlayer.setRotationYawHead(Objects.requireNonNull(Minecraft.getInstance().player).rotationYawHead);
		fakePlayer.spawn();
		savedPerspective = Objects.requireNonNull(Minecraft.getInstance().gameSettings).thirdPersonView;
		Objects.requireNonNull(Minecraft.getInstance().gameSettings).thirdPersonView = 0;
		net.minecraft.client.Minecraft.getInstance().setRenderViewEntity(fakePlayer);
		if (Minecraft.getInstance().player.movementInput instanceof MovementInputFromOptions) Minecraft.getInstance().player.movementInput = new DummyInput();
	}

	public static boolean isCameraEntity(Entity entity) {
		return entity.getMinecraftEntity() == fakePlayer;
	}

	public static void disable() {
		Minecraft.getInstance().gameSettings.thirdPersonView = savedPerspective;
		net.minecraft.client.Minecraft.getInstance().setRenderViewEntity(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player));
		if (fakePlayer != null) fakePlayer.despawn();
		fakePlayer = null;
		if (Minecraft.getInstance().player.movementInput instanceof DummyInput) Minecraft.getInstance().player.movementInput = new MovementInputFromOptions(Minecraft.getInstance().gameSettings);
	}

}
