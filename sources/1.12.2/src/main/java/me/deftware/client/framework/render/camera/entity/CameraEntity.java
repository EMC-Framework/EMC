package me.deftware.client.framework.render.camera.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

/**
 * @author wagyourtail, Deftware
 */
@SuppressWarnings("EntityConstructor")
public class CameraEntity extends EntityOtherPlayerMP {

	public MovementInput input;

	public CameraEntity(WorldClient clientWorld, GameProfile gameProfile, FoodStats hunger) {
		super(clientWorld, gameProfile);
		this.foodStats = hunger;
		Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
		this.input = new MovementInputFromOptions(mc.gameSettings);
	}

	@Override
	public boolean isInRangeToRender3d(double cameraX, double cameraY, double cameraZ) {
		return false;
	}

	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return false;
	}

	@Override
	public boolean getAlwaysRenderNameTagForRender() {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.setVelocity(0, 0, 0);

		input.updatePlayerMoveState();

		float upDown = (this.input.sneak ? -CameraEntityMan.speed : 0) + (this.input.jump ? CameraEntityMan.speed : 0);

		Vec3d forward = new Vec3d(0, 0, CameraEntityMan.speed * 2.5).rotateYaw(-(float) Math.toRadians(this.rotationYawHead));
		Vec3d strafe = forward.rotateYaw((float) Math.toRadians(90));
		Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ);

		motion = motion.add(0, 2 * upDown, 0);
		motion = motion.add(strafe.x * input.moveStrafe, 0, strafe.z * input.moveStrafe);
		motion = motion.add(forward.x * input.moveForward, 0, forward.z * input.moveForward);

		this.setPosition(this.posX + motion.x, this.posY + motion.y, this.posZ + motion.z);
	}

	public void spawn() {
		Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
		Objects.requireNonNull(mc.world).addEntityToWorld(this.getEntityId(), this);
	}

	public void despawn() {
		Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
		Objects.requireNonNull(mc.world).removeEntityFromWorld(this.getEntityId());
	}

}
