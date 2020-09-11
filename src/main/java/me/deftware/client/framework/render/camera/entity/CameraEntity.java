package me.deftware.client.framework.render.camera.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

/**
 * @author wagyourtail, Deftware
 */
@SuppressWarnings("EntityConstructor")
public class CameraEntity extends OtherClientPlayerEntity {

	public Input input;

	public CameraEntity(ClientWorld clientWorld, GameProfile gameProfile, HungerManager hunger) {
		super(clientWorld, gameProfile);
		this.hungerManager = hunger;
		Minecraft mc = net.minecraft.client.Minecraft.getInstance();
		this.input = new KeyboardInput(mc.options);
	}

	@Override
	public boolean shouldRenderFrom(double cameraX, double cameraY, double cameraZ) {
		return false;
	}

	@Override
	public boolean shouldRenderAtDistance(double distance) {
		return false;
	}

	@Override
	public boolean shouldRenderName() {
		return false;
	}

	@Override
	public void tickMovement() {
		this.setVelocity(0, 0, 0);

		input.tick(false, false);

		float upDown = (this.input.sneaking ? -CameraEntityMan.speed : 0) + (this.input.jumping ? CameraEntityMan.speed : 0);

		Vec3d forward = new Vec3d(0, 0, CameraEntityMan.speed * 2.5).rotateY(-(float) Math.toRadians(this.headYaw));
		Vec3d strafe = forward.rotateY((float) Math.toRadians(90));
		Vec3d motion = this.getVelocity();

		motion = motion.add(0, 2 * upDown, 0);
		motion = motion.add(strafe.x * input.movementSideways, 0, strafe.z * input.movementSideways);
		motion = motion.add(forward.x * input.movementForward, 0, forward.z * input.movementForward);

		this.setPosition(this.x + motion.x, this.y + motion.y, this.z + motion.z);
	}

	public void spawn() {
		Minecraft mc = net.minecraft.client.Minecraft.getInstance();
		Objects.requireNonNull(mc.world).addEntity(this.getEntityId(), this);
	}

	public void despawn() {
		Minecraft mc = net.minecraft.client.Minecraft.getInstance();
		Objects.requireNonNull(mc.world).removeEntity(this.getEntityId());
	}

}
