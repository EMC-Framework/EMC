package me.deftware.client.framework.render.camera.entity;

import com.mojang.authlib.GameProfile;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.phys.Vec3;

/**
 * @author wagyourtail, Deftware
 */
public class CameraEntity extends RemotePlayer {

	private static final AtomicInteger NEXT_CAMERA_ID = new AtomicInteger(-10000);

	public ClientInput input;

	public CameraEntity(ClientLevel clientWorld, GameProfile gameProfile, FoodData hunger) {
		super(clientWorld, gameProfile);
		this.setId(NEXT_CAMERA_ID.getAndDecrement());
		this.foodData = hunger;
		Minecraft mc = Minecraft.getInstance();
		this.input = new KeyboardInput(mc.options);
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return false;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return false;
	}

	@Override
	public boolean shouldShowName() {
		return false;
	}

	@Override
	public void aiStep() {
		this.setDeltaMovement(0, 0, 0);

		input.tick();

		var sneaking = this.input.keyPresses.shift();
		var jumping = this.input.keyPresses.jump();
		float upDown = (sneaking ? -CameraEntityMan.speed : 0) + (jumping ? CameraEntityMan.speed : 0);

		Vec3 forward = new Vec3(0, 0, CameraEntityMan.speed * 2.5).yRot(-(float) Math.toRadians(this.yHeadRot));
		Vec3 strafe = forward.yRot((float) Math.toRadians(90));
		Vec3 motion = this.getDeltaMovement();

		var movement = input.getMoveVector();
		motion = motion.add(0, 2 * upDown, 0);
		// TODO: Verify
		motion = motion.add(strafe.x * movement.y, 0, strafe.z * movement.y);
		motion = motion.add(forward.x * movement.x, 0, forward.z * movement.x);

		this.setPosRaw(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
	}

	public void spawn() {
		Minecraft mc = Minecraft.getInstance();
		Objects.requireNonNull(mc.level).addEntity(this);
	}

	public void despawn() {
		Minecraft mc = Minecraft.getInstance();
		Objects.requireNonNull(mc.level).removeEntity(this.getId(), RemovalReason.DISCARDED); // TODO: Verify this
	}

}
